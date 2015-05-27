//Evaluator class
//Comp 141 Assignment 7
//Neha Tammana
//=========================================================
// public entry point to Evaluator module
//=========================================================

import java.io.*;

public class Evaluator {
	public static SymbolTable symtable = new SymbolTable();
	public Evaluator(){}
	public static SchemeObject evaluateExpression(SchemeObject _expression) throws IOException
	{
		if (_expression == null) return null;

		// functions, atoms and () evaluate to themselves
		else if ((_expression instanceof Function) ||
			     (_expression instanceof  BooleanAtom) ||
	             (_expression instanceof  IntegerAtom) ||
	             (_expression instanceof  RealAtom)||
	             (_expression instanceof EmptyList))
		   return _expression;

		// symbols evaluate by lookup from the symbol table
		else if (_expression instanceof  SymbolAtom)
	    {
	        SchemeObject symb_value = symtable.lookup(((SymbolAtom)_expression).getValue());
	        if (symb_value == null) throw new IOException("undefined symbol cannot be evaluated");
	        return symb_value;
	    }

		// lists are special forms or function applications
	    else if (_expression instanceof Pair)
		{
	    	
		    Pair pair = (Pair)_expression;

			// head of the list must be a special form name, function name, or a function object
	        SchemeObject form_or_function = pair.getCar();
			
			// cdr is the rest of the expression, which is a pair-list of arguments to the form or function
			Pair arguments;
			if (pair.getCdr() == null)
				arguments = null;
			else if (pair.getCdr() instanceof  Pair)
				arguments = (Pair)pair.getCdr();
			else
				throw new IOException("expression has badly formed argument list"); 

			// check for a symbol that maps to a special form or function
			if (form_or_function instanceof SymbolAtom)
			{
				String name = ((SymbolAtom)form_or_function).getValue();
				
				// check for special forms
				if (name.equals("define")) return evaluateDefineForm(arguments);
				if (name.equals("quote"))  return evaluateQuoteForm(arguments);
				if (name.equals("let"))    return evaluateLetForm(arguments);
				if (name.equals("lambda")) return evaluateLambdaForm(arguments);
				if (name.equals("if"))     return evaluateIfForm(arguments);

				Pair evaluated_arguments = evaluateListElements(arguments);

				// check for built-in functions
				if (name.equals("plus")) return applyPlus(evaluated_arguments);
				if (name.equals("lessthan")) return applyLessthan(evaluated_arguments);
				if (name.equals("car")) return applyCar(evaluated_arguments);
				if (name.equals("cdr")) return applyCdr(evaluated_arguments);
				if (name.equals("cons")) return applyCons(evaluated_arguments);
				if (name.equals("isnull")) return applyIsnull(evaluated_arguments);

				// if name is not predefined, look it up. apply it if it is a function.
				SchemeObject function = symtable.lookup(name);
				if ((function != null) && (function instanceof  Function))
					return applyFunction((Function)function, evaluated_arguments);
				else
					throw new IOException("expression does not start with a function");
			}
			else
			{
				// if first term is not a symbol, it must be an anonymous lambda expression.
				if (!(form_or_function instanceof Pair))
					throw new IOException("expected a lambda expression, got something else");
				SchemeObject lambda = evaluateExpression(form_or_function);
				if ((lambda == null) || (!(lambda instanceof Function))) 
					throw new IOException("expected a lambda expression, got something else");
				return applyFunction((Function)lambda, arguments);
			}
		}

		throw new IOException("attempt to evaluate unknown object type (this should never happen!)");
	}
	//=========================================================
	// helper functions for dealing with pair-lists
	//=========================================================

	// Recursively evaluate the elements of a pair-list and build a new pair-list from the results.
	static Pair evaluateListElements(Pair _list) throws IOException
	{
		if (_list == null) return null;

		Pair newPair = new Pair();
		newPair.setCar(evaluateExpression(_list.getCar()));

		SchemeObject rest = _list.getCdr();
		if ((rest != null) && (!(rest instanceof Pair))) 
			throw new IOException("badly formed argument list");
		newPair.setCdr(evaluateListElements((Pair)rest));
		
		return newPair;
	}

	// Check if a pair-list consists entirely of integer atoms
	static boolean allIntegers(Pair _list)
	{
		Pair arg = (Pair)_list;
	    while (arg != null)
	    {
			if (!(arg.getCar() instanceof IntegerAtom)) return false;
	        arg = (Pair)arg.getCdr();
		}
		return true;
	}

	//=========================================================
	// implementations of special forms
	//=========================================================

	public static SchemeObject evaluateLetForm(Pair _arguments) throws IOException
	{
		// special form: 1st param is list of bindings, 2nd param is expression to 
		//      evaluate in context of those bindings.
		if (_arguments.length() != 2) throw new IOException("incorrect number of arguments to let");

		SchemeObject car = _arguments.getCar();
		SchemeObject cdr = _arguments.getCdr();
		SchemeObject binding_list = car;
		if (!(cdr instanceof Pair))	throw new IOException("bad parameter list to let");
		Pair rest = (Pair)cdr;
		car = rest.getCar();
		cdr = rest.getCdr();
		SchemeObject body = car;

		symtable.pushScope();

		// process the bindings
		if (!(binding_list instanceof Pair)) throw new IOException("bad parameter list to let");
		Pair binding_iter = (Pair)binding_list;
		while (true)
		{
			SchemeObject binding = binding_iter.getCar();

			if (!(binding instanceof Pair))
				throw new IOException("bad parameter list to let");
			if (((Pair)binding).length() != 2)
				throw new IOException("invalid binding given to let");
			SchemeObject b_symbol = ((Pair)binding).getCar();
			SchemeObject b_expr = ((Pair)binding).getCdr();

			if (!(b_symbol instanceof SymbolAtom))	throw new IOException("bad parameter list to let");
			if (!(b_expr instanceof Pair)) throw new IOException("bad parameter list to let");
			
			SchemeObject b_value = evaluateExpression(((Pair)b_expr).getCar());
			symtable.defineLocal(((SymbolAtom)b_symbol).getValue(), b_value);

			SchemeObject next_binding = binding_iter.getCdr();
			if (next_binding == null) break;

			if (!(next_binding instanceof Pair)) throw new IOException("bad parameter list to let");

			binding_iter = (Pair)next_binding;
		}
		
		SchemeObject result = evaluateExpression(body);
		symtable.popScope();
		return result;
	}

	public static SchemeObject evaluateDefineForm(Pair _arguments) throws IOException
	{
		// special form: 1st param is unevaluated symbol, 2nd param is evaluated.
		if (_arguments.length() != 2) throw new IOException("incorrect number of arguments to define");

		SchemeObject arg1 = _arguments.getCar();
		SchemeObject rest = _arguments.getCdr();
		
		if (!(arg1 instanceof SymbolAtom))	throw new IOException("bad argument to define");
		if (!(rest instanceof Pair)) throw new IOException("bad argument to define");

		String symb_name = ((SymbolAtom)arg1).getValue();
		SchemeObject arg2 = ((Pair)rest).getCar();
		SchemeObject value = evaluateExpression(arg2);
		symtable.defineGlobal(symb_name, value);
		return null;
	}

	public static SchemeObject evaluateQuoteForm(Pair _arguments) throws IOException
	{
		// special form: 1st param is unevaluated result, only 1 param excepted.
		if (_arguments.length() != 1) throw new IOException("incorrect number of arguments to quote");

		return _arguments.getCar();
	}

	public static SchemeObject evaluateIfForm(Pair _arguments) throws IOException
	{
		// special form: 1st param is boolean test, 2nd param is consequent and, 3rd parameter is alternate.
		if (_arguments.length() != 3) throw new IOException("incorrect number of arguments to define");

		SchemeObject test = _arguments.getCar();
		Pair rest = (Pair)_arguments.getCdr();
		SchemeObject consequent = rest.getCar();
		rest = (Pair)rest.getCdr();
		SchemeObject alternate = rest.getCar();

		SchemeObject condition = evaluateExpression(test);
		if (condition == null) 
			throw new IOException("error evaluating test condition in if form");
		if ((condition instanceof BooleanAtom) && (((BooleanAtom)condition).getValue() == false))
			return evaluateExpression(alternate);
		else
			return evaluateExpression(consequent);
	}

	public static SchemeObject evaluateLambdaForm(Pair _arguments) throws IOException
	{
		// special form: 1st param is list of symbols which are the formal parameters.
		//      2nd param is the Function body.
		if (_arguments.length() != 2) throw new IOException("incorrect number of arguments to lambda");

		if ((_arguments.getCar() == null) || (!(_arguments.getCar() instanceof Pair)))
			throw new IOException("invalid parameter list to lambda");
		Pair formal_parameters = (Pair)_arguments.getCar();
		SchemeObject cdr = _arguments.getCdr();

		if (cdr == null) throw new IOException("bad parameter list to let");
		if (!(cdr instanceof Pair))	throw new IOException("bad parameter list to let");

		SchemeObject body = ((Pair)cdr).getCar();
		
		// Check that formal_parameters contains only symbols	
		Pair arg = formal_parameters;
		while (arg != null)
		{
			if (!(arg.getCar() instanceof SymbolAtom))
				throw new IOException("invalid parameter list to lambda");
			if (arg.getCdr() == null) 
				break;
			if (!(arg.getCdr() instanceof Pair)) 
				throw new IOException("invalid parameter list to lambda");
			arg = (Pair)arg.getCdr();
		}
		Function new_func = new Function();
		new_func.setBody(body);
		new_func.setParameters(arg);
		return new_func;
	}

	//=========================================================
	// apply a Function object (lambda Function)  
	//=========================================================

	// _func must be a Function object
	// _argument must be Pair-list contining evaluated arguments
	public static SchemeObject applyFunction(Function _func, Pair _arguments) throws IOException
	{
		Pair parameters = _func.getParameters();
		if (parameters == null) System.out.println("null parameters");
		if (_arguments == null) System.out.println("null arguments");
		if (parameters.length() != _arguments.length()) 
			
			throw new IOException("argument list length does not match parameter list length");

		// create a scope block, define the parameters with argument values
		symtable.pushScope();

		Pair p = parameters;
		Pair a = _arguments;
		while (p != null)
		{
			String name = ((SymbolAtom)(p.getCar())).getValue();
			SchemeObject value= a.getCar();
			symtable.defineLocal(name, value);
			p = (Pair)p.getCdr();
			a = (Pair)a.getCdr();
		}
		
		// evaluate the body in the scope of the parameters
		SchemeObject result = evaluateExpression(_func.getBody());

		// destroy the parameter scope block
		symtable.popScope();

		return result;
	}

	//=========================================================
	// implementations of predefined Function
	//=========================================================

	public static SchemeObject applyPlus(Pair _arguments) throws IOException
	{
		if (allIntegers(_arguments))
		{
			// do integer math
		    Pair arg = (Pair)_arguments;
		    long result = 0;
		    while (arg != null)
		    {
		        SchemeObject car = arg.getCar();
	            result += ((IntegerAtom)car).getValue();
		        arg = (Pair)arg.getCdr();
		    }
			return new IntegerAtom(result);
		}
		else
		{
			// do real number math
		    Pair arg = (Pair)_arguments;
		    double result = 0.0;
		    while (arg != null)
		    {
		        double p = 0.0;
		        SchemeObject car = arg.getCar();
		        if (car instanceof IntegerAtom)
		            p = (double) (((IntegerAtom)car).getValue());
		        else if (car instanceof RealAtom)
		            p = ((RealAtom)car).getValue();
		        else
		            throw new IOException("invalid parameter to plus");
		        result += p;
		        arg = (Pair)arg.getCdr();
		    }
			return new RealAtom(result);
		}
	}

	public static SchemeObject applyLessthan(Pair _arguments) throws IOException
	{
		if (_arguments.length() != 2) throw new IOException("incorrect number of arguments to lessthan");
		SchemeObject arg1 = _arguments.getCar();
		SchemeObject arg2 = ((Pair)_arguments.getCdr()).getCar();

		if (((!(arg1 instanceof IntegerAtom)) && (!(arg1 instanceof RealAtom)) ||
			((!(arg2 instanceof IntegerAtom)) && (!(arg2 instanceof RealAtom)))))
			throw new IOException("invalid argument types to lessthan");

		double v1, v2;
		if (arg1 instanceof IntegerAtom) v1 = (double)((IntegerAtom)arg1).getValue();
		else v1 = ((RealAtom)arg1).getValue();
		if (arg2 instanceof IntegerAtom) v2 = (double)((IntegerAtom)arg2).getValue();
		else v2 = ((RealAtom)arg2).getValue();	
		return new BooleanAtom(v1 < v2);
	}

	public static SchemeObject applyCar(Pair _arguments) throws IOException
	{
		if (_arguments.length() != 1) throw new IOException("incorrect number of arguments to car");
		SchemeObject arg1 = _arguments.getCar();

		if (!(arg1 instanceof Pair)) throw new IOException("invalid argument types to car");

		return ((Pair)arg1).getCar();
	}

	public static SchemeObject applyCdr(Pair _arguments) throws IOException
	{
		if (_arguments.length() != 1) throw new IOException("incorrect number of arguments to cdr");
		SchemeObject arg1 = _arguments.getCar();

		if (!(arg1 instanceof Pair)) throw new IOException("invalid argument types to cdr");

		SchemeObject cdr = ((Pair)arg1).getCdr();
		if (cdr == null) return new EmptyList();
		return cdr;
	}

	public static SchemeObject applyCons(Pair _arguments) throws IOException
	{
		if (_arguments.length() != 2) throw new IOException("incorrect number of arguments to cons");
		SchemeObject arg1 = _arguments.getCar();
		SchemeObject arg2 = ((Pair)_arguments.getCdr()).getCar();

		if (arg2 instanceof Pair) 
			return new Pair(arg1, arg2);		
		else if (arg2 instanceof EmptyList)
			return new Pair(arg1, null);	
		else
			throw new IOException("invalid argument types to cons");
	}

	public static SchemeObject applyIsnull(Pair _arguments) throws IOException
	{
		if (_arguments.length() != 1) throw new IOException("incorrect number of arguments to isnull");
		SchemeObject arg1 = _arguments.getCar();
		return new BooleanAtom(arg1 instanceof EmptyList);
	}
	}
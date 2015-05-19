//Comp 141 Assignment 7
//Neha Tammana
//Contains main function 

import java.util.*;
import java.io.*;


class Interpreter {
	private static final boolean SHOW_INPUT=true;
	private static final boolean SHOW_SCANNER_OUTPUT=false;
	private static final boolean SHOW_PARSER_OUTPUT=false;
	
	static private void printTokenList(List<Token> tokens)
	{
		Iterator iter= tokens.iterator();
		while (iter.hasNext())
		{
			Token token = (Token)iter.next();
			if(token instanceof SymbolToken)
			{
				SymbolToken sToken = (SymbolToken)token;
			//case SymbolToken:
				System.out.println("symbol:       " + sToken.getValue());
				//break;
			}
			/*else if(token instanceof BooleanToken)
			{
				BooleanToken bToken = (BooleanToken)token;
			//case SymbolToken:
				System.out.println("boolean:       " + bToken.getValue());
				//break;
			}*/
			else if (token instanceof IntegerToken)
			{
				IntegerToken itoken = (IntegerToken)token;
				System.out.println("integer:      " + itoken.getValue());
			}
			/*else if (token instanceof RealToken)
			{
				RealToken rtoken = (RealToken)token;
				System.out.println("real:         " + rtoken.getValue());
			}*/
			/*else if (token instanceof PunctuationToken)
			{
				PunctuationToken ptoken = (PunctuationToken)token;
				System.out.println("punctuation:  " + ptoken.getValue());
			}*/
		}
	}
	// ================================================================
	// functions for displaying parser results (tree of SchemeObjects)
	// ================================================================
	static void printSpaces(short spaces)
	{
	    for (short i=0; i<spaces; i++) System.out.println(" ");
	}

	// Print the parse tree in an inverted form.
	// Pairs are identified by their address, to help
	// match the car and cdr in the printed tree.
	// (It is not necessary to have this sort of identifier
	// in your code, but it is useful for debugging.)
	static void printParseTree(SchemeObject root, short spaces)
	{
		if (root == null)
		{
		   printSpaces(spaces);
		   System.out.print("null \n");
		}
		else if (root instanceof SymbolAtom)
		{
		   printSpaces(spaces);
		   System.out.print( "Symbol:" + ((SymbolAtom)root).getValue() + "\n");
		}
		else if (root instanceof BooleanAtom)
		{
		   printSpaces(spaces);
		   System.out.print("Boolean:");
		   if (((BooleanAtom)root).getValue()) System.out.println("true \n");
		   else System.out.println("false \n");
		}
		else if (root instanceof IntegerAtom)
		{
		   printSpaces(spaces);
		   System.out.print( "Integer:" + ((IntegerAtom)root).getValue() + "\n");
		}
		else if (root instanceof RealAtom)
		{
		   printSpaces(spaces);
		   System.out.println("Real:" + ((RealAtom)root).getValue() + "\n");
		}
		else if (root instanceof EmptyList)
		{
		   printSpaces(spaces);
		   System.out.println("EmptyList:() \n");
		}
		else if (root instanceof Pair)
		{
			Pair pair = (Pair)root;
		    printSpaces(spaces);
		    System.out.println("Pair[" + pair + "].car: \n");
		    short x = (short) (spaces+2);
			printParseTree(pair.getCar(), x);
		    printSpaces(spaces);
		    System.out.println("Pair[" + pair + "].cdr: \n");
			printParseTree(pair.getCdr(), x);
		}
		else if (root instanceof Function)
		{
		    printSpaces(spaces);
			System.out.print("FUNCTION \n");
		}
	}
	
	static void printParseTreeAsList(SchemeObject root, boolean was_car)
	{
		if (root == null) 
			   return; 
			else if (root instanceof SymbolAtom)
				System.out.println("Symbol:" + ((SymbolAtom)root).getValue());
			else if (root instanceof BooleanAtom)
			{
			   System.out.print("Boolean:");
			   if (((BooleanAtom)root).getValue()) System.out.print("true");
			   else System.out.print("false");
			}
			else if (root instanceof IntegerAtom)
				System.out.println("Integer:" + ((IntegerAtom)root).getValue());
			else if (root instanceof RealAtom)
				System.out.println("Real:" + ((RealAtom)root).getValue());
			else if (root instanceof EmptyList)
			   System.out.print("EmptyList:()");
			else if (root instanceof Pair)
			{
				Pair pair = (Pair)root;
		        if (was_car) System.out.print("(");
				printParseTreeAsList(pair.getCar(), true);
				if (pair.getCdr() != null)
				{
					System.out.print(" ");
					printParseTreeAsList(pair.getCdr(), false);
				}
				if (was_car) System.out.print(")");
			}
		else if (root instanceof Function)
		   System.out.print("FUNCTION \n");
	}

	// ================================================================
	// function for displaying a Scheme expression
	// ================================================================
	static void printSchemeExpression(SchemeObject root, boolean was_car)
	{
		if (root == null) return;

		else if (root instanceof SymbolAtom)
		   System.out.print(((SymbolAtom)root).getValue());

		else if (root instanceof BooleanAtom)
		{
			if (((BooleanAtom)root).getValue()) System.out.print("#t");
			else {
				System.out.print("#f");
				}
		}

		else if (root instanceof IntegerAtom)
		   System.out.print(((IntegerAtom)root).getValue());

		else if (root instanceof RealAtom)
		   System.out.print(((RealAtom)root).getValue());

		else if (root instanceof EmptyList)
		   System.out.print("()");

		else if (root instanceof Pair)
		{
			Pair pair = (Pair)root;
	        if (was_car) System.out.print("(");
			printSchemeExpression(pair.getCar(), true);
			if (pair.getCdr() != null)
			{
				System.out.print(" ");
				printSchemeExpression(pair.getCdr(), false);
			}
			if (was_car) System.out.print(")");
		}
		else if (root instanceof Function)
		    System.out.print("FUNCTION \n");
	}
	
	
static public void main(String[]args) throws IOException, ScannerException
{
	Scanner sc = new Scanner(System.in);
	System.out.print("SCHEME: ");
	while (sc.hasNextLine())
	{
		String expr = sc.nextLine();
		if (expr.length() < 1) break;
		System.out.println("Expression as input: ");
		System.out.println(expr);
		List<Token> tokens = null;
		SchemeObject parse_tree = null;

		try
		{
			SchemeScanner scanner = new SchemeScanner();
			tokens = scanner.scanExpression(expr);
			System.out.println("Scanner Result:");
			printTokenList(tokens);
		}
		catch (ScannerException se)
		{
			System.out.println(se.getMessage());
			continue;
		}
		try
		{
			Parser parser = new Parser();
			parse_tree = parser.parse(tokens);
			System.out.println("Parser Result:");
			printSchemeExpression(parse_tree, true);
			System.out.println();
		}
		catch (ParserException pe)
		{
			System.out.println(pe.getMessage());
		}

		/**result always null when in try**/
		SchemeObject result = null;
		//SchemeObject result = new SchemeObject();
		try
		{
            result = Evaluator.evaluateExpression(parse_tree);
		}
		catch (IOException ee)
		{
			System.out.println();
			System.out.print("Evaluator error: " + ee.getMessage());
			continue;
		}
		if (SHOW_INPUT || SHOW_SCANNER_OUTPUT || SHOW_PARSER_OUTPUT)
			System.out.println("Result expression: \n");
		printSchemeExpression(result, true);
		if (SHOW_INPUT || SHOW_SCANNER_OUTPUT || SHOW_PARSER_OUTPUT)
		{
			System.out.println("\n");
		}
		
		System.out.println();
		System.out.print("SCHEME: ");
	}
		sc.close();
	}
}

	// ================================================================
	// interpreter main function
	// ================================================================
/*	int main()
	{
	    Evaluator evaluator;
		while (true)
		{
			cout << endl << "SCHEME: ";
			char line[3000];
			cin.getline(line,3000);
			string expr(line);
			if (expr.length() == 0) break;
			if (SHOW_INPUT)
			{
				cout << endl << "Input Expression:" << endl;
				cout << expr << endl;
			}

			list<Token> tokens;
			try
			{
				tokens = scanExpression(expr);
				if (SHOW_SCANNER_OUTPUT)
				{
					cout << endl << "Scanner Result:" << endl;
					printTokenList(tokens);
					cout << endl;
				}
			}
			catch (ScannerException se)
			{
				cout << endl << "Scanner error: " << se.getMessage() << endl;
				continue;
			}
			SchemeObject parse_tree = null;
			try
			{
				parse_tree = parseExpression(tokens);
				if (SHOW_PARSER_OUTPUT)
				{
					//cout << endl<< "Parser Result in tree form:" << endl;
					//printParseTree(parse_tree);
					cout << endl<< "Parser Result in list form:" << endl;
					printParseTreeAsList(parse_tree);
					cout << endl;
				}
			}
			catch (ParserException pe)
			{
				cout << endl << "Parser error: " << pe.getMessage() << endl;
				continue;
			}
			SchemeObject result = null;
			try
			{
	            result = evaluator.evaluateExpression(parse_tree);
			}
			catch (EvaluatorException ee)
			{
				cout << endl << "Evaluator error: " << ee.getMessage() << endl;
				continue;
			}
			if (SHOW_INPUT || SHOW_SCANNER_OUTPUT || SHOW_PARSER_OUTPUT)
				cout << endl<< "Result expression:" << endl;
			printSchemeExpression(result);
			if (SHOW_INPUT || SHOW_SCANNER_OUTPUT || SHOW_PARSER_OUTPUT)
				cout << endl;
		}
		return 0;
	}

}*/

//Comp 141 Assignment 7
//Neha Tammana

// Parser.java - Parser module for a simplified version of Scheme.
// COMP141 - Spring 2015 - HW 6 Instructor's  Solution
// Author: Michael Doherty
//
// A Parser object receives a list of Token pointers
//   representing a simplified Scheme expression and generates
//   an abstract syntax tree representing the expression.

// BNF grammar for our language:
// <expression> ::= <atom> | ( {<expression> } )  | ' <expression>
// <atom> ::= SYMBOL | BOOLEAN | INTEGER | REAL
//
// '<expression> is semantically equivalent to ( <quote> <expression> ),
// where <quote> is a symbol token with value "quote". The parser will parse any
// occurence of '<expression> as if it were ( <quote> <expression> ).
import java.util.List;
import java.util.Iterator;

class Parser
{
	public SchemeObject parse(List<Token> _tokens) throws ParserException
	{
		tokens = _tokens;
		token_iter = tokens.iterator();
		consumeToken();

		SchemeObject etree = parseExpression();
		if (curr_token != null)
			throw new ParserException("unconsumed tokens at end of expression");
		return etree;
	}

	private	SchemeObject parseExpression() throws ParserException
	{
		// check for a ' quoted expression
	/*	if ((curr_token instanceof PunctuationToken) &&
		    (((PunctuationToken)curr_token).getValue() == '\''))
		{
			consumeToken(); // consume the '
			// replace ' <expr> with ( quote <expr> )
			return new Pair(new SymbolAtom(new String("quote")), new Pair(parseExpression()));
		}

		// A list expression starts with a (
		if ((curr_token instanceof PunctuationToken) &&
	        (((PunctuationToken)curr_token).getValue() == '('))
	    {
	        consumeToken();  // consume the (
			if (curr_token==null) throw new ParserException("Unclosed List");
	        if ((curr_token instanceof PunctuationToken) &&
	            (((PunctuationToken)curr_token).getValue() == ')'))
			{
				consumeToken(); // consume the )
				return new EmptyList();
			}

			Pair front_pair = null;
			Pair back_pair = null;
	        while (!(curr_token instanceof PunctuationToken) ||
	               (((PunctuationToken)curr_token).getValue() != ')'))
	        {
				// parse next expression in list
				SchemeObject next_expr = parseExpression();

				// create a pair for the current expression
				Pair this_pair = new Pair(next_expr, null);

				// connect the new pair to the end of the list
				if (front_pair == null) front_pair = this_pair;
				if (back_pair != null) back_pair.setCdr(this_pair);
				back_pair = this_pair;

	            if (curr_token==null) throw new ParserException("Unclosed List");
	        }
	        consumeToken(); // consume the )
	        return front_pair;
	    }

		// if there was no (, it must be an atom
	    else											*/
	        return parseAtom();
	}

	private SchemeObject parseAtom() throws ParserException
	{
		SchemeObject thisnode = null;
		if (curr_token instanceof SymbolToken)
	    {
	        String s = ((SymbolToken)curr_token).getValue();
	        thisnode = new SymbolAtom(s);
		}
		/*else if (curr_token instanceof BooleanToken)
		{
			boolean n = ((BooleanToken)curr_token).getValue();
			thisnode = new BooleanAtom(n);
		}*/
		else if (curr_token instanceof IntegerToken)
		{
			long n = ((IntegerToken)curr_token).getValue();
			thisnode = new IntegerAtom(n);
		}
		/*else if (curr_token instanceof RealToken)
		{
			double n = ((RealToken)curr_token).getValue();
			thisnode = new RealAtom(n);
		}*/

		if (thisnode != null)
		{
			consumeToken();
			return thisnode;
		}
		else
	    {
	        throw new ParserException("Invalid Atom");
	    }
	}

	private void consumeToken()
	{
		if (token_iter.hasNext()) curr_token = token_iter.next();
		else curr_token = null;
	}

	private List<Token> tokens = null;
	private Iterator<Token> token_iter = null;
	private Token curr_token = null;
}


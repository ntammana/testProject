//Comp 141 Assignment 7
//Neha Tammana

// SchemeScanner.java - scanner module for simplified version of Scheme
// COMP141 - Spring 2015 - HW 5 Instructor's  Solution
// Author: Michael Doherty

import java.util.List;
import java.util.ArrayList;

class SchemeScanner
{

// entry point for the module

	public List<Token> scanExpression(String expression) throws ScannerException
	{
		// reset all attributes
		tokens = new ArrayList<Token>();
		expr = expression;
		end_of_expr = false;
		cursor = -1;
		advanceCursor();

		// scan tokens and store them in a list
		Token token = null;
		do {
			token = getNextToken();
			if (token != null) tokens.add(token);
		} while (token != null);

		return tokens;
	}

// methods to control cursor indicating progress through expression string

	private void advanceCursor()
	{
		cursor++;
		if (cursor >= expr.length()) end_of_expr = true;
		else next_char = expr.charAt(cursor);
	}

	private void skipWhiteSpace()
	{
		while (!end_of_expr && isWhiteSpace(next_char))
			advanceCursor();
	}

// scanning methods

	private Token getNextToken() throws ScannerException
	{
		Token token = null;
		skipWhiteSpace();
		if (end_of_expr)
		{
			token = null;
		}
		/*else if (isPunctuation(next_char))
		{
			token = scanPunctuation();
		}*/
		/*else if (next_char == '#')
		{
			token = scanBoolean();
		}*/
		else if (isAlpha(next_char))
		{
			token =  scanSymbol();
		}
		else if (isNumeric(next_char))
		{
			token = scanNumber();
		}
		else
		{
			throw new ScannerException(new String("Invalid Character: ") + next_char);
		}
		return token;
	}

	/*private Token scanPunctuation()
	{
		Token token = new PunctuationToken(next_char);
		advanceCursor();
		return token;
	}*/

	private Token scanBoolean() throws ScannerException
	{
		if (next_char != '#')
			throw new ScannerException(new String("badly formed boolean - expecting #"));
		advanceCursor();
		boolean value = false;
		if (next_char == 't')
			value = true;
		else if (next_char == 'f')
			value = false;
		else
			throw new ScannerException(new String("badly formed boolean - expecting t or f"));
		advanceCursor();
		return new BooleanToken(value);
	}

	private Token scanSymbol() throws ScannerException
	{
	    String symbol = new String();
	    if (!end_of_expr && isAlpha(next_char))
	    {
			symbol += next_char;
			advanceCursor();
		}
		else
			throw new ScannerException(new String("badly formed symbol - expecting initial letter"));
	    while (!end_of_expr && (isAlpha(next_char) || isDigit(next_char)))
	    {
	        symbol += next_char;
	 		advanceCursor();
	    }
	    return new SymbolToken(symbol);
	}

	private Token scanNumber() throws ScannerException
	{
		boolean found_a_digit = false;
		boolean found_a_decimal_point = false;
		long whole_part = 0;
		double fract_part = 0.0;
		double fract_multiplier = 0.1;
		long sign = 1;
		if (next_char == '+')
			advanceCursor();
		else if (next_char == '-')
		{
			sign = -1;
			advanceCursor();
		}

		while (!end_of_expr && ((next_char == '.') || isDigit(next_char)))
		{
			if (next_char == '.')
			{
				if (found_a_decimal_point)
					throw new ScannerException(new String("badly formed number - multiple decimal points"));
				found_a_decimal_point = true;
			}
			else
			{
				found_a_digit = true;
				if (!found_a_decimal_point)
				{
					whole_part = whole_part*10 + next_char-'0';
				}
				else
				{
					fract_part = fract_part + (next_char-'0')*fract_multiplier;
					fract_multiplier = fract_multiplier/10.0;
				}
			}
			advanceCursor();
		}
		if (!found_a_digit)
	        throw new ScannerException(new String("badly formed number - no digits"));
	/*	if (found_a_decimal_point)
			return new RealToken(sign*(whole_part+fract_part));*/
		else
			return new IntegerToken(sign*whole_part);
	}

// attributes

	private	List<Token> tokens;
	private short cursor;
	private boolean end_of_expr;
	private char next_char;
	private String expr;

// various utility methods

	private boolean isWhiteSpace(char c)
	{
		return ((c == ' ') || (c == '\t') || (c == '\n'));
	}
	private boolean isPunctuation(char c)
	{
		return ((c == '(') || (c == ')') || (c == '\''));
	}

	private boolean isAlpha(char c)
	{
		return (((c>='a')&&(c<='z')) || ((c>='A')&&(c<='Z')));
	}

	private boolean isNumeric(char c)
	{
		return ((c == '.') || (c == '+') || (c == '-') || isDigit(c));
	}

	private boolean isDigit(char c)
	{
		return ((c >= '0') && (c <= '9'));
	}

}



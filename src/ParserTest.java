//Comp 141 Assignment 7
//Neha Tammana

// ParserTest.java - test driver for scanner module
// COMP141 - Spring 2015 - HW 6 Instructor's  Solution
// Author: Michael Doherty

import java.util.Scanner;
import java.util.List;
import java.util.Iterator;

class ParserTest
{

// ================================================================
// function for displaying a token list
// ================================================================
	static private void printTokenList(List<Token> tokens)
	{
		Iterator iter = tokens.iterator();
		while (iter.hasNext())
		{
			Token token = (Token)iter.next();
			if (token instanceof SymbolToken)
			{
				SymbolToken stoken = (SymbolToken)token;
				System.out.println("symbol:       " + stoken.getValue());
			}
			/*else if (token instanceof BooleanToken)
			{
				BooleanToken btoken = (BooleanToken)token;
				System.out.println("boolean:      " + btoken.getValue());
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
// function for displaying a Scheme expression
// ================================================================
	static private void printSchemeExpression(SchemeObject root, boolean was_car)
	{
		if (root == null) return;

		else if (root instanceof SymbolAtom)
		   System.out.print(((SymbolAtom)root).getValue());

		else if (root instanceof BooleanAtom)
		{
			if (((BooleanAtom)root).getValue()) System.out.print("#t");
			System.out.print("#f");
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
	}

	static public void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("SCHEME: ");
		while (sc.hasNextLine())
		{
			String expr = sc.nextLine();
			if (expr.length() < 1) break;
			System.out.println("Expression as input:");
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
			}
			catch (ParserException pe)
			{
				System.out.println(pe.getMessage());
			}

			System.out.println();
			System.out.print("SCHEME: ");
		}
	}
}
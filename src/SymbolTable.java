//Comp 141 Assignment 7
//Neha Tammana

import java.util.*;

class ScopeBlock{
	
		private Map<String, SchemeObject> table;
	    public ScopeBlock() {table = new HashMap<String, SchemeObject>();}

		// define stores the object pointed at by definition
	    public void define(String symbol, SchemeObject definition)
	    {
	    	
	    	System.out.println(symbol);
	    	if (table.containsKey(symbol))
	    	{
	    		table.remove(symbol);
	    	}
	    	
	    	table.put(symbol, definition);
	    }
	    
		// lookup returns clones of stored objects
	    SchemeObject lookup(String symbol)
	    {
	    	return table.get(symbol);
	    }
	   
}

class SymbolTable {
		    SymbolTable() {}

		// defineLocal stores the object pointed at by definition in the most recent local scope block.
		// If there is no local scope block, the definition is put in the global scope block.
	    void defineLocal(String symbol, SchemeObject definition)
	    {
	    	
			if (scope_stack.isEmpty())
				global_scope.define(symbol, definition);
			else
				scope_stack.get(symbol.length()-1).define(symbol, definition);
	    }

		// defineGlobal stores the object pointed at by definition in the global scope block
	    void defineGlobal(String symbol, SchemeObject definition)
	    {
			global_scope.define(symbol, definition);
	    }

		// lookup returns clones of stored objects
	    public SchemeObject lookup(String symbol)
	    {
			SchemeObject def = null;

			// first look in any local scope blocks
			int i = scope_stack.size()-1;
			while (i >= 0)
			{
				def = (scope_stack.get(i)).lookup(symbol);
				if (def != null) break;
				i--;
			}

			// if not found in local scope blocks, look in global scope block
			if (def == null)
				def = global_scope.lookup(symbol);

			return def;
	    }

		public void pushScope() { scope_stack.add(new ScopeBlock());	}

		public void popScope()	{ scope_stack.remove(scope_stack.size()-1); }

	
	    public ScopeBlock global_scope = new ScopeBlock();
		public ArrayList<ScopeBlock> scope_stack= new ArrayList();
	}



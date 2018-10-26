import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
/* Node class */ 
class Node{
	public int count;
	public String item;
	public List<Node> children;
	public Node parent;
	public Node() {}
	public Node(String item) {
		count=1;
		this.item=item;
		children=new ArrayList<>();
		parent=null;
	}
}
/*Table Node Class*/
class Table_Node{
	public String item;
	public int count;
	public List<Node> nextNodes;
	public Table_Node() {
		
	}
	public Table_Node(String it,int c) {
		item=it;
		count=c;
		nextNodes=new ArrayList<>();
	}
	public List<Table_Node> createTable_Node ( LinkedHashMap<List<String>,Integer> C) {
		List<Table_Node> t=new ArrayList<>();
		for(List<String> l:C.keySet()) {
			t.add(new Table_Node(l.get(0),C.get(l)));
		}
		return t;
	}
}
/*FP Growth Class*/
class FPTree {
	Node fptree=new Node("X");
	LinkedHashMap< List<String>,Integer> items=new LinkedHashMap<>();/*candidate list2*/
	int min_sup=2;
	/*insert a transaction in tree*/
	void insert(Node T,List<String> t,List<Table_Node> Table_Node) {
		 if (!T.children.isEmpty()) {
	            for (Node child : T.children) {
	                if (child.item.equals(t.get(0))) {
	                    child.count+=1;
	                    t.remove(t.get(0));
	                    if (!t.isEmpty()) {
	                        insert(child, t,Table_Node);
	                    }
	                    return;
	                }
	            }
	            Node newNode = new Node(t.get(0));
	            for(Table_Node tab:Table_Node) {
	            	if(tab.item.equals(newNode.item))
	            	{
	            		tab.nextNodes.add(newNode);
	            	}
	            }
	            t.remove(t.get(0));
	            T.children.add(newNode);
	            
	            newNode.parent=T;
	            if (!t.isEmpty()) {
	                insert(newNode, t,Table_Node);
	            }
	        } else {
	        	if(t.size()!=0)
	            {
	        		Node newNode = new Node(t.get(0));
	        		for(Table_Node tab:Table_Node) {
		            	if(tab.item.equals(newNode.item))
		            	{
		            		tab.nextNodes.add(newNode);
		            	}
		            }
		            T.children.add(newNode);
		            newNode.parent=T;
		            t.remove(t.get(0));
		            if (!t.isEmpty()) {
		                insert(newNode, t,Table_Node);
		            }
	            }
	     }
	}
	/*check list L1 contains list L2 or not*/
	boolean isSubList(List l1,List l2) {
		boolean flag=true;
		for(int i=0;i<l2.size();i++) {
			if(!l1.contains(l2.get(i))) {
				flag=false;
			}
		}
		return flag;
	}
	/*arrange a transaction in decreasing order of support count*/
	List<String> arrange(List<String> l1,List<String> l2) {
		List<String> l=new ArrayList<>();
		for(int i=0;i<l2.size();i++) {
			if(l1.contains(l2.get(i))) {
				l.add(l2.get(i));
			}
		}
		return l;
	}
	/*generate candidate list with given transactions & minimum support */
	void getCandiCount(List<List<String>> trans,LinkedHashMap<List<String>,Integer> C) {
		for(List<String>  ll: C.keySet()) {
			int count=0;
			for(int t=0;t<trans.size();t++) {
				if(isSubList(trans.get(t),ll)) {
					count++;
				}
			}
			C.put(ll,count);
		}
	}
	/*display candidate list form given List and form next level List*/
	void displayC_Form_L(LinkedHashMap<List<String>, Integer> C, List<List<String>> L1, int h,int min_sup) {
		if(!C.isEmpty())
			System.out.println("Condidate items C"+h);
		for(List<String>  ll: C.keySet()) {
			System.out.println(ll+" : "+C.get(ll).intValue());
			if(C.get(ll).intValue()>=min_sup) {
				L1.add(ll);
			}
		}
	}
	/*reverse a list of strings*/
	Set<String> reverse1(Set<String> lst2){
		Stack<String> st=new Stack<>();
		for(String s:lst2) {
			st.push(s);
		}
		Set<String> lst=new LinkedHashSet<>();
		while(!st.isEmpty()) {
			lst.add(st.pop());
		}
		return lst;
	}
	/*making tree with given transactions*/
	void makeTree(Node T,List<List<String>> trans,List<Table_Node> Table_Node) {
		for(int i=0;i<trans.size();i++) {	
			insert(T,trans.get(i),Table_Node);
		}
	}
	/*display transactions*/
	void displayTranscations(List<List<String>> trans) {
		for(int i=0;i<trans.size();i++) {
			int j=i+1;
			System.out.println("T"+j+":"+trans.get(i));
		}
	}
	void readTransactions(LinkedHashMap< List<String>,Integer> C,List<List<String>> trans) throws Exception {
		Scanner sc=new Scanner(System.in);		
		System.out.print("Enter file name(csv file):");
		String fname=sc.nextLine();
		FileReader fr=new FileReader(fname);
		BufferedReader br=new BufferedReader(fr);
		String str;
		String ite[]=br.readLine().split(",");
		for(String s:ite) {
			List<String> l=new ArrayList<>();
			l.add(s);
			C.put(l, 0);
		}
		while((str=br.readLine())!=null) {
			String tr[]=str.split(",");
			int p=0;
			List<String> li=new ArrayList<>();
			for(String s:tr) {
				if(s.equals("")) {
					p++;
				}
				else {
					li.add(ite[p++]);
				}
			}
			trans.add(li);
		}
		br.close();
	}
	void displayC(LinkedHashMap< List<String>,Integer> C) {
		for(List<String> li:C.keySet()) {
			System.out.println(li+":"+C.get(li));
		}
	}
	LinkedHashMap< List<String>,Integer> reverseSort(LinkedHashMap< List<String>,Integer> C){
		return C.entrySet().stream()
		        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x,y)-> {throw new AssertionError();},LinkedHashMap::new
		        ));
	}
	/*find frequent item list*/
	LinkedHashMap< List<String>,Integer> findL(LinkedHashMap< List<String>,Integer> C,int min_sup){
		LinkedHashMap< List<String>,Integer> CC=new LinkedHashMap<>();
		for(List<String> l:C.keySet()) {
			//System.out.println(l+"****&&&&******"+C.get(l));
			if(C.get(l)>=min_sup){
				CC.put(l, C.get(l));
			}
		}
		return CC;
	}
	List<String> findArrangeList(LinkedHashMap< List<String>,Integer> C) {
		List<String> temp=new ArrayList<>();
		for(List<String> l:C.keySet()) {
			temp.add(l.get(0));
		}
		return temp;
	}
	void arrangeTransactions(List<List<String>> trans,List<String> temp) {
		for(int i=0;i<trans.size();i++) {
			trans.set(i,arrange(trans.get(i), temp));
		}
	}
	/*find number of leaves*/
	int countLeaves(Node tree) {
		if(tree.children.size()==0)
			return 1;
		else {
			int res=0;
			for(int i=0;i<tree.children.size();i++) {
				res+=countLeaves(tree.children.get(i));
			}
			return res;
		}
	}
	/*check whether tree contains single path or more*/
	boolean onlySinglePath(Node tree) {
		if(countLeaves(tree)<=1) {
			return true;
		}
		else {
			return false;
		}
	}
	/*find sublists for a given list*/
	LinkedHashMap<List<String>,Integer> findSubList(LinkedHashMap<String,Integer> li1) {
		int n = li1.size();
		List<String> li=new ArrayList<>();
		for(String s:li1.keySet()) {
			li.add(s);
		}
		LinkedHashMap<List<String>,Integer> lst=new LinkedHashMap<>();
		for (int i = 0; i < (1<<n); i++) 
		{
			List<String> l=new ArrayList<>();
			int c=0;
			for (int j = 0; j < n; j++) 
			if ((i & (1 << j)) > 0) 
			{
				l.add(li.get(j));
				c=li1.get(li.get(j));
			}
			lst.put(l,c);
		}
		return lst;
	}
	/*construct condition base pattern*/
	LinkedHashMap<Set<String>, Integer> constructCondiBasePatt(List<Table_Node> table,Node tree,List<String> b) {
		LinkedHashMap<Set<String>, Integer> cfp=new LinkedHashMap<>();
		for(int i=0;i<table.size();i++) {
			Table_Node tab=table.get(i);
			if(b.get(0).equals(tab.item)) {
				int cou=tab.nextNodes.get(0).count;
				for(Node n:tab.nextNodes) {
					cou=n.count;
					Set<String> lst=new LinkedHashSet<>();
					n=n.parent;
					while(n.parent!=null) {
						lst.add(n.item);
						n=n.parent;
					}
					if(lst.size()>0)
					{
						lst=reverse1(lst);
						cfp.put(lst, cou);
					}
				}
				List<String> itli= new ArrayList<>();
				itli.add(tab.item);
			}
		}
		return cfp;
	}
	void printTable(List<Table_Node> table) {
		for(int i=0;i<table.size();i++) {
			System.out.println(table.get(i).item+" "+table.get(i).count+" "+table.get(i).nextNodes.size());
		}
	}
	void FpGrowth(Node tree,List<Table_Node> table,List<String> a,LinkedHashMap<List<String>,Integer> freqitems,int sup) {
		LinkedHashMap<Set<String>, Integer> cond_pat_base=new LinkedHashMap<>();
		if(onlySinglePath(tree)) {
			LinkedHashMap<String,Integer> list=new LinkedHashMap<>();
			while(tree.children.size()!=0) {
				list.put(tree.children.get(0).item,tree.children.get(0).count);
				tree=tree.children.get(0);
			}
			LinkedHashMap<List<String>,Integer> lst=findSubList(list);
			for(List<String> l:lst.keySet()) {
				int count=lst.get(l);
				if(l.size()==0) {
					if(a!=null)
					{	
						l.addAll(a);
						count=sup;
					}
					if(l.size()!=0) {
						freqitems.put(l,count);
					}
				}
				else {
					if(a!=null)
					{	
						l.addAll(a);
					}
					freqitems.put(l,count);
				}
					
			}
		}
		else {
			for(int i=table.size()-1;i>=0;i--) {
				List<String> b=new ArrayList<>();
				b.add(table.get(i).item);
				cond_pat_base=constructCondiBasePatt(table, tree,b);
				if(a!=null)
					b.addAll(a);
				int support=table.get(i).count;
				/*C without support count*/
				LinkedHashMap< List<String>,Integer> C=new LinkedHashMap<>();
				C=items;
				List<List<String>> trans=new ArrayList<>();/*list of transaction*/
				for(Set<String> s:cond_pat_base.keySet()) {
					List<String> lst=new ArrayList<>();
					for(String str:s) {
						lst.add(str);
					}
					int c=cond_pat_base.get(s);
					for(int p=0;p<c;p++)
						trans.add(lst);
				}
				getCandiCount(trans, C);
				C=reverseSort(C);/*sort in candidate item list in reverse  order*/
				C=findL(C,min_sup);/*remove all non-frequent items*/
				List<String> temp=findArrangeList(C);
				/*arrange transaction*/
				arrangeTransactions(trans,temp);
				/*create f p tree*/
				Table_Node t=new Table_Node();
				List<Table_Node> table1=t.createTable_Node(C);
				Node x=new Node("X");
				makeTree(x, trans, table1);
				if(x.children.size()!=0)
				{
					FpGrowth(x, table1, b, freqitems,support);
				}
				else {
					freqitems.put(b, support);
				}
			}
		}
	}
	/*print frequent item lists*/
	void printFrequentItems(LinkedHashMap< List<String>,Integer> freqitems) {
		for(List<String> ll:freqitems.keySet()) {
			System.out.println(ll+"\t:"+freqitems.get(ll));
		}
	}
}
public class FPGrowth{
	public static void main(String[] args) throws Exception {
		FPTree f=new FPTree();/*object of FPTree class*/
		Scanner sc=new Scanner(System.in);
		List<List<String>> trans=new ArrayList<>();/*list for transactions*/
		LinkedHashMap< List<String>,Integer> C=new LinkedHashMap<>();/*candidate list1*/
		/*list of frequent items and their support count*/
		LinkedHashMap< List<String>,Integer> freqitems=new LinkedHashMap<>();
		f.readTransactions(C, trans);/*read csv file and name transactions*/
		f.displayTranscations(trans);/*display transactions*/
		System.out.print("enter minimum support:");
		f.min_sup=sc.nextInt();
		f.getCandiCount(trans, C);/*get count of each candidate*/
		System.out.println("Condidate list C ");
		f.displayC(C);/*display candidate item list*/
		C=f.reverseSort(C);/*sort in candidate item list in reverse  order*/
		C=f.findL(C,f.min_sup);/*remove all non-frequent items*/
		for(List<String> li:C.keySet()) {
			freqitems.put(li, C.get(li));
		}
		List<String> temp=f.findArrangeList(C);
		f.items=C;
		System.out.println("L1 in reverse sorted order ");
		f.displayC(C);
		/*arrange transaction*/
		f.arrangeTransactions(trans,temp);
		/*create f p tree*/
		Table_Node t=new Table_Node();
		List<Table_Node> table=t.createTable_Node(C);
		f.makeTree(f.fptree, trans, table);
		/*traversing tree for mining frequent item sets*/
		f.FpGrowth(f.fptree, table, null, freqitems,f.min_sup);
		/*print all frequent item sets*/
		System.out.println("\n***frequent item sets are***");
		f.printFrequentItems(freqitems);
	}
}

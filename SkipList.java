import java.util.Random;

@SuppressWarnings("unchecked")
public class SkipList<T extends Comparable<? super T>> {

	public int maxLevel;
	public SkipListNode<T>[] root;
	private int[] powers;
	private Random rd = new Random();

	SkipList(int i) {
		maxLevel = i;
		root = new SkipListNode[maxLevel];
		powers = new int[maxLevel];
		for (int j = 0; j < i; j++)
			root[j] = null;
		choosePowers();
		rd.setSeed(202003); // do not modify this line
	}

	SkipList() {
		this(4);
	}

	public void choosePowers() {
		powers[maxLevel - 1] = (2 << (maxLevel - 1)) - 1;
		for (int i = maxLevel - 2, j = 0; i >= 0; i--, j++)
			powers[i] = powers[i + 1] - (2 << j);
	}

	public int chooseLevel() {
		int i, r = Math.abs(rd.nextInt()) % powers[maxLevel - 1] + 1;
		for (i = 1; i < maxLevel; i++) {
			if (r < powers[i])
				return i - 1;
		}
		return i - 1;
	}

	public boolean isEmpty() {
		// your code goes here
		return root[0] ==null;
	}

	public void insert(T key) {
		// your code goes here
		SkipListNode<T> [] current= new SkipListNode[maxLevel];
		SkipListNode<T> [] previous= new SkipListNode[maxLevel];
		SkipListNode<T> newNode;
		int level, i;
		current[maxLevel-1]=root[maxLevel-1];
		previous[maxLevel-1]=null;
		for(level =maxLevel-1;level>=0;level--) {
			while (current[level] != null && current[level].key.compareTo(key) < 0) {
				previous[level] = current[level];
				current[level] = current[level].next[level];
			}//end of while
			if (current[level] != null && current[level].key.equals(key)) {
				return;
			}
			if (level > 0)
				if (previous[level] == null) {
					current[level - 1] = root[level - 1];
					previous[level - 1] = null;

				} else {
					current[level - 1] = previous[level].next[level - 1];
					previous[level - 1] = previous[level];
				}
		}
		level=chooseLevel();
		newNode=new SkipListNode<>(key,level+1);
		for(i=0;i<=level;i++){
			newNode.next[i]=current[i];
			if(previous[i]==null){
				root[i]=newNode;
			}
			else{
				previous[i].next[i]=newNode;
			}

		}

	}

	public boolean delete(T key) {
		// your code goes here
		int level;
		SkipListNode<T> previous,current,trans;
		for(level=maxLevel-1;level>=0 && root[level]==null;level--);
		previous=current=root[level];
		boolean status=false;
		while(level>=0){

			if(key.equals(current.key) ){

				if(current==root[level]){
					System.out.println("im here");

					root[level] = current.next[level];
					level--;
					//current=previous.next[level];

				}
				else {
					previous.next[level] = current.next[level];
					level--;

					while (level >= 0 && previous.next[level].key.compareTo(key) < 0) {
						previous = previous.next[level];
					}
				}
				if(level<0){
					status=true;
				}




			}
			else if(key.compareTo(current.key)<0){
				if(level==0)
					status= false;
				else if(current==root[level])
					current=root[--level];
				else
					current=previous.next[--level];
			}
			else{
				previous=current;
				if(current.next[level]!=null)
					current=current.next[level];
				else{
					for (level--;level>=0&& current.next[level]==null;level--);
					if(level>=0)
						current=current.next[level];
					else status= false;
				}
			}
		}
		return status;
	}

	public T first() {
		// your code goes here
		if(isEmpty())
			return null;
		else
			return root[0].key;
	}

	public T search(T key) {
		// your code goes here
		int level;
		SkipListNode<T> previous,current;
		for(level=maxLevel-1;level>=0 && root[level]==null;level--);
		previous=current=root[level];
		while(true){
			if(key.equals(current.key))
				return current.key;
			else if(key.compareTo(current.key)<0){
				if(level==0)
					return null;
				else if(current==root[level])
					current=root[--level];
				else
					current=previous.next[--level];
			}
			else{
				previous=current;
				if(current.next[level]!=null)
					current=current.next[level];
				else{
					for (level--;level>=0&& current.next[level]==null;level--);
					if(level>=0)
						current=current.next[level];
					else return null;
				}
			}
		}
	}
	
	public String getPathToLastNode() {
		// your code goes here

		SkipListNode<T> trans;
		trans = root[0];
		while (trans.next[0] != null) {
			trans=trans.next[0];
		}
		T key= trans.key;

		//searching
		String path="";
		int level;
		SkipListNode<T> previous,current;
		for(level=maxLevel-1;level>=0 && root[level]==null;level--);
		previous=current=root[level];
		while(true){
			if(key.equals(current.key)){
				path+="[" + current.key + "]";
				return path;}
			else if(key.compareTo(current.key)<0){ //check if its less than
				if(level==0)
					return null;
				else if(current==root[level])
					current=root[--level];
				else {
					path+= "["+ previous.key +"]";
					current = previous.next[--level];
				}
			}
			else{// otherwise its greater
				previous=current;
				if(current.next[level]!=null){
					path+="[" + current.key +"]";
					current=current.next[level];
				}
				else{
					for (level--;level>=0&& current.next[level]==null;level--);
					if(level>=0) {
						path += "[" + current.key + "]";
						current = current.next[level];
					}
					else
						return path;
				}
			}
		}


	}

}
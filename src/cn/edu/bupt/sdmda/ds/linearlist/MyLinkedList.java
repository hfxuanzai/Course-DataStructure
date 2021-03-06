package cn.edu.bupt.sdmda.ds.linearlist;

public class MyLinkedList<T> implements LinearList<T>{

	public class Node{
		public T _ele;
		public Node _next;
		public Node(){
			init(null, null);
		}
		public Node(T e){
			init(e, null);
		}
		public Node(T e, Node n){
			init(e, n);
		}
		private void init(T e, Node n){
			_ele = e;
			_next = n;
		}
	}

	protected Node _head;
	protected int _size;

	public MyLinkedList(int s, T init) {
		init(s, init);
	}

	public MyLinkedList() {
		init(0, null);
	}

	@Override
	public void init(int s, T init) {
		_size = s;
		_head = new Node();
		Node cur = _head;
		for ( int i = 0 ; i < s ; i ++ ) {
			Node newnode = new Node(init);
			cur._next = newnode;
			cur = newnode;
		}
	}

	@Override
	public boolean isEmpty() {
		return _size == 0;
	}

	@Override
	public int getSize() {
		return _size;
	}

	@Override
	public void clear() {
		_size = 0;
		Node cur = _head;
		Node next = _head._next;
		while ( next != null ) {
			cur._next = null;
			cur = next;
			next = cur._next;
		}
	}

	@Override
	public void insert(int i, T t) {
		if ( ! checkWritableRange(i) ) {
			throw new IndexOutOfBoundsException();
		}
		_size ++;
		Node cur = _head;
		while ( i -- > 0 ) cur = cur._next;
		Node tmp = new Node(t);
		tmp._next = cur._next;
		cur._next = tmp;
	}

	@Override
	public void delete(T t) {
		_size --;
		Node cur = _head;
		while ( cur._next != null ) {
			if ( cur._next._ele.equals(t) ) {
				Node newnext = cur._next._next;
				cur._next._next = null;
				cur._next = newnext;
				break;
			}
			cur = cur._next;
		}
	}
	
	@Override
	public T deleteAt(int i) {
		if ( ! checkReadableRange(i) ) {
			throw new IndexOutOfBoundsException();
		}
		_size --;
		Node cur = _head;
		while ( i -- > 0 ) cur = cur._next;
		Node tmp = cur._next;
		cur._next = cur._next._next;
		tmp._next = null;
		return tmp._ele;
	}

	@Override
	public T get(int i) {
		if ( ! checkReadableRange(i) ) {
			throw new IndexOutOfBoundsException();
		}
		Node cur = _head;
		while ( i -- > 0 ) cur = cur._next;
		return cur._next._ele;
	}

	@Override
	public void set(int i, T t) {
		if ( ! checkReadableRange(i) ) {
			throw new IndexOutOfBoundsException();
		}
		Node cur = _head;
		while ( i -- > 0 ) cur = cur._next;
		cur._next._ele = t;
	}

	@Override
	public int find(T t) {
		Node cur = _head;
		int i = 0;
		while ( cur._next != null ) {
			if ( cur._next._ele.equals(t) ) return i;
			cur = cur._next;
			i ++;
		}
		return -1;
	}

	@Override
	public LinearList<T> sort() {
		return null;
	}
	
	@Override
	public String toString() {
		String res = "[";
		Node cur = _head;
		for(int i = 0 ; i < getSize() ; i ++) {
			res += (cur._next._ele.toString() + ", ");
			cur = cur._next;
		}
		if(res.length() > 1)
			res = res.substring(0, res.length()-2);
		res += "]";
		return res;
	}

	private boolean checkReadableRange(int i){
		return ( i >= 0 && i < getSize() );
	}

	private boolean checkWritableRange(int i){
		return ( i >= 0 && i <= getSize() );
	}


}

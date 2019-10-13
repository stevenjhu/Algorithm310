package edu.nyu.cs.sh5005;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class Solution {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out,"ASCII"),4096);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		HashMap<String,Candidate> map = new HashMap<String,Candidate>();
				
		int entry = Integer.parseInt(in.readLine());
		MinHeap h = new MinHeap(entry);
		
		for (int i=0;i<=entry;i++) {
			String[] input = in.readLine().split(" ");
			String key = input[0];
			int value = Integer.parseInt(input[1]);
			h.insert(key, value, map);
		}
		
		
		
		int command = Integer.parseInt(in.readLine());
		for (int i=0;i<command;i++) {
			String[] input = in.readLine().split(" ");
			if (Integer.parseInt(input[0])==1) { //type 1
				String key = input[1];
				int delta = Integer.parseInt(input[2]);
				edit(key,delta,map,h);
				
			}else { //type 2
				int threshold = Integer.parseInt(input[1]);
				int minCP = h.heap[1].cp;
				int index = 1;
				while(minCP<threshold) {
					h.pop(map);
					minCP = h.heap[++index].cp;
				}
				out.write(map.size());
			}
			
		}
		out.flush();
	}
	static void edit(String key, int delta, HashMap<String,Candidate> map, MinHeap h) {
		Candidate a = map.get(key);
		a.cp+=delta;
		if(delta>0) {
			h.heapifyDown(a.pos);
		}else if(delta<0) {
			h.heapifyUp(a.pos);
		}
	}
}

class Candidate {
	String name;
	int cp;
	int pos;
	
	Candidate(){
		name="";
		cp=0;
	}
	Candidate(String name, int cp, int pos){
		this.name=name;
		this.cp=cp;
		this.pos=pos;
	}
}
class MinHeap{
	int maxSize;
	int size;
	Candidate[] heap;
	final int headPos = 1;

	private int parent(int pos) {
		return (pos/2);
	}
	private int left(int pos) {
		return (pos*2);
	}
	private int right(int pos) {
		return (pos*2+1);
	}
	
	//constructor
	MinHeap(int maxSize){
		this.maxSize=maxSize;
		heap=new Candidate [maxSize+1];
		//heap starts at 1
		heap[0]=null;
		size=1;
	}
	//swap
	void swap(int posA, int posB) {
		Candidate temp = heap[posA];
		heap[posA]=heap[posB];
		heap[posB]=temp;
		heap[posA].pos = posA;
		heap[posB].pos = posB;
	}
	//restore
	void heapifyUp(int pos) {//when inserting
		int c= heap[pos].cp;
		if(parent(pos)<=0) {
			return;
		}
		int cParent=heap[parent(pos)].cp;
		if(c<cParent) {
			swap(pos,parent(pos));
			heapifyUp(parent(pos));
		}
	}
	void heapifyDown(int pos) {
		int i = pos;
	
		if(left(i)<size&&heap[left(i)].cp<heap[i].cp) {//if left child exist(last index size-1) and left child value is smaller than current value
			swap(i,left(i));
			heapifyDown(left(i));
		}else if(right(i)<size&&heap[right(i)].cp<heap[i].cp) {//if right child exist and right child value is smaller than current value
			swap(i,right(i));
			heapifyDown(right(i));
		}else {
			return;
		}
	}
	void insert(String name, int cp, HashMap<String,Candidate> map) {//insert at the end and heapifyUp
		Candidate a = new Candidate(name,cp,size);
		map.put(a.name,a);
		heap[size] = a;
		heapifyUp(size);
		size++;
	}
	
	Candidate pop(HashMap<String,Candidate> map) {
		Candidate removed = heap[1];
		//remove from heap
		swap(size-1,1);
		heap[size-1]=null;
		size--;
		heapifyDown(1);
		
		//remove from hashmap
		map.remove(removed.name);
		
		return removed;
	}
	
}

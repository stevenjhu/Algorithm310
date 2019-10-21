package edu.nyu.cs.sh5005;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import java.util.HashMap;

public class Solution {

    public static void main(String[] args) throws Exception{
        // TODO Auto-generated method stub
        //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out,"ASCII"),4096);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        HashMap<String,Candidate> map = new HashMap<String,Candidate>();
                
        int entry = Integer.parseInt(in.readLine());
        MinHeap h = new MinHeap(entry);
        
        for (int i=0;i<entry;i++) {
            String[] input = in.readLine().split(" ");
            String key = input[0];
            long value = Integer.parseInt(input[1]);
            h.insert(key, value, map);
        }

        
        
        int command = Integer.parseInt(in.readLine());
        for (int i=0;i<command;i++) {
            String[] input = in.readLine().split(" ");
            if (Integer.parseInt(input[0])==1) { //type 1
                String key = input[1];
                long delta = Integer.parseInt(input[2]);
                edit(key,delta,map,h);
                
            }else { //type 2
                long threshold = Integer.parseInt(input[1]);
                long minCP = h.heap[1].cp;
                int index = 1;
                while(minCP<threshold) {
                    h.pop(map);
                    minCP = h.heap[index++].cp;
                }
                System.out.println(map.size());
                //out.write(map.size());
            }
            
        }
        //out.flush();
    }
    static void edit(String key, long delta, HashMap<String,Candidate> map, MinHeap h) {
        Candidate a = map.get(key);
        a.cp+=delta;
        int leftPos = h.left(a.pos);
        int rightPos = h.right(a.pos);
        if((leftPos!=-1&&a.cp>h.heap[leftPos].cp)||(rightPos!=-1&&a.cp>h.heap[rightPos].cp)) { //if new cp > either child's cp, heapifyDown
            h.heapifyDown(a.pos);
        }else if(h.parent(a.pos)!=-1&&a.cp< h.heap[h.parent(a.pos)].cp) { // < parent cp, then heapifyUp
            h.heapifyUp(a.pos);
        }
    }
}

class Candidate {
    String name;
    long cp;
    int pos;
    
    Candidate(){
        name="";
        cp=0;
    }
    Candidate(String name, long cp, int pos){
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

    int parent(int pos) {
        return ((pos/2)>0)?(pos/2):-1;
    }
    int left(int pos) {
        return ((pos*2)<size)?(pos*2):-1;
    }
    int right(int pos) {
        return ((pos*2+1)<size)?(pos*2+1):-1;
    }
    
    //constructor
    MinHeap(int maxSize){
        this.maxSize=maxSize+1;
        heap=new Candidate [this.maxSize];
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
        long tempCp= heap[pos].cp;
        if(parent(pos)==-1) {
            return;
        }
        long tempParent=heap[parent(pos)].cp;
        if(tempCp<tempParent) {
            swap(pos,parent(pos));
            heapifyUp(parent(pos));
        }
    }
    void heapifyDown(int pos) {
        if(left(pos)==-1&&right(pos)==-1) {
            return;
        }
        long leftCP = heap[left(pos)].cp;
        //int rightCP = heap[right(pos)].cp;
        if(right(pos)==-1||leftCP<heap[right(pos)].cp) {
            swap(pos,left(pos));
            heapifyDown(left(pos));
        }else {
            swap(pos,right(pos));
            heapifyDown(right(pos));
        }
    }
    void insert(String name, long cp, HashMap<String,Candidate> map) {//insert at the end and heapifyUp
        Candidate a = new Candidate(name,cp,size);
        map.put(a.name,a);
        heap[size] = a;
        heapifyUp(size);
        size++;
    }
    
    Candidate pop(HashMap<String,Candidate> map) {
        Candidate removed = heap[1];
        //remove from heap
        swap(--size,1);
        heap[size]=null;
        heapifyDown(1);
        
        //remove from hashmap
        map.remove(removed.name);
        
        return removed;
    }
    
}

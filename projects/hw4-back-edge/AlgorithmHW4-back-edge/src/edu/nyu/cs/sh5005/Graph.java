package edu.nyu.cs.sh5005;

class Edge {
	   int succ;
	   Edge next;

	   Edge(int succ, Edge next) {
	      this.succ = succ;
	      this.next = next;
	   }
	}

	class Graph {
	   Edge[] A; 
	   // A[u] points to the head of a liked list;
	   // p in the list corresponds to an edge u -> p.succ in the graph

	   Graph(int n) {
	      // initialize a graph with n vertices and no edges
	      A = new Edge[n];
	   }

	   void addEdge(int u, int v) {
	      // add an edge i -> j to the graph

	      A[u] = new Edge(v, A[u]);
	   }
	}

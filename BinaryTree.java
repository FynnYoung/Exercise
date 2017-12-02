import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

class TreeNode{
	protected int value;
	protected TreeNode leftChild;
	protected TreeNode rightChild;
	protected int maxLeftDepth = -1;
	protected int maxRightDepth = -1;
	protected int maxDistance = -1;
	
	public TreeNode(){
		
	}
	public TreeNode(int v){
		value = v;
	}
}
public class BinaryTree {
	/**
	 * pre-order and in-order traversal together,
	 * or post-order and in-order traversal together can determine one unique tree,
	 * but pre-order and post-order together cannot.
	 * 
	 * @param pre
	 * @param in
	 * @return
	 */
	public TreeNode orderPreIn(int[] pre, int[] in){
		TreeNode root = new TreeNode();
		root.value = pre[0];
		addChildren(root, pre, in);
		return root;
	}
	public void addChildren(TreeNode parent, int[] pre, int[] in){
		if(pre.length>0 && in.length>0){
			int ri = rootIndex(pre[0], in);
			if(ri>0){
				int[] leftPre = Arrays.copyOfRange(pre, 1, ri+1);
				int[] leftIn = Arrays.copyOfRange(in, 0, ri);
				TreeNode left = new TreeNode();
				left.value = leftPre[0];
				parent.leftChild = left;
				addChildren(left, leftPre, leftIn);
			}
			if(ri<in.length-1){
				int[] rightPre = Arrays.copyOfRange(pre, ri+1, pre.length);
				int[] rightIn = Arrays.copyOfRange(in, ri+1, in.length);
				TreeNode right = new TreeNode();
				right.value = rightPre[0];
				parent.rightChild = right;
				addChildren(right, rightPre, rightIn);
			}
		}
	}
	public int rootIndex(int root, int[] in){
		int index = 0;
		for(index=0; index<in.length; index++){
			if(in[index]==root) break;
		}
		return index;
	}
	
	public boolean hasSubtree(TreeNode tree, TreeNode node){
		boolean result = false;
		if(tree!=null && node!=null){
			if(tree.value==node.value){
				result = doesTree1HaveTree2(tree, node);
			}
			if(!result) result = hasSubtree(tree.leftChild, node);
			if(!result) result = hasSubtree(tree.rightChild, node);
		}
		return result;
	}
	public boolean doesTree1HaveTree2(TreeNode tree, TreeNode node){
		if(node==null) return true;
		if(tree==null) return false;
		if(tree.value!=node.value) return false;
		return doesTree1HaveTree2(tree.leftChild, node.leftChild) && doesTree1HaveTree2(tree.rightChild, node.rightChild);
	}
	
	protected TreeNode lastNodeInList;
	public TreeNode convertToList(TreeNode root){
		lastNodeInList = null;
		convertNode(root);
		TreeNode head = lastNodeInList;
		while(head!=null && head.leftChild!=null){
			head = head.leftChild;
		}
		return head;
	}
	public void convertNode(TreeNode node){
		if(node==null) return;
		TreeNode current = node;
		if(current.leftChild!=null){
			convertNode(current.leftChild);
		}
		current.leftChild = lastNodeInList;
		if(lastNodeInList!=null){
			lastNodeInList.rightChild = current;
		}
		lastNodeInList = current;
		if(current.rightChild!=null){
			convertNode(current.rightChild);
		}
	}
	
	protected static boolean isBalanced = true;
	public void isBalancedTree(TreeNode root){
		isBalancedNode(root);
		System.out.println(isBalanced);
	}
	public int isBalancedNode(TreeNode node){
		if(node==null){
			return 0;
		}
		int leftDepth = 0, rightDepth = 0;
		if(isBalanced) leftDepth = isBalancedNode(node.leftChild);
		if(isBalanced) rightDepth = isBalancedNode(node.rightChild);
		if(isBalanced){
			int diff = leftDepth-rightDepth;
			if(diff<=1 && diff>=-1){
				return 1+(leftDepth>rightDepth?leftDepth:rightDepth);
			}else{
				isBalanced = false;
			}
		}
		return 0;
	}
	
	public int maxDistanceOfNodesRecursive(TreeNode node){
		int leftMax = 0;
		int rightMax = 0;
		if(node.leftChild==null){
			node.maxLeftDepth = 0;
		}else{
			maxDistanceOfNodesRecursive(node.leftChild);
			node.maxLeftDepth = Math.max(node.leftChild.maxLeftDepth, node.leftChild.maxRightDepth)+1;
			leftMax = node.leftChild.maxLeftDepth+node.leftChild.maxRightDepth;
		}
		if(node.rightChild==null){
			node.maxRightDepth = 0;
		}else{
			maxDistanceOfNodesRecursive(node.rightChild);
			node.maxRightDepth = Math.max(node.rightChild.maxLeftDepth, node.rightChild.maxRightDepth)+1;
			rightMax = node.rightChild.maxLeftDepth+node.rightChild.maxRightDepth;
		}
		System.out.println(node.value+" "+node.maxLeftDepth+" "+node.maxRightDepth);
		return Math.max(Math.max(leftMax, rightMax), node.maxLeftDepth+node.maxRightDepth);
	}
	public int maxDistanceOfNodesIteration(TreeNode root){
		if(root==null)
			return -1;
		
		Stack<TreeNode> s = new Stack<TreeNode>();
		TreeNode node = root;
		while(node!=null || !s.isEmpty()){
			while(node!=null && node.maxLeftDepth<0){
				s.push(node);
				node = node.leftChild;
			}
			//System.out.println(s);
			if(!s.isEmpty()){
				node = s.pop();
				if(node.maxLeftDepth<0){
					if(node.leftChild==null){
						node.maxLeftDepth = 0;
					}else{
						node.maxLeftDepth = Math.max(node.leftChild.maxLeftDepth, node.leftChild.maxRightDepth)+1;
						
					}
				}
				if(node.maxRightDepth<0){
					if(node.rightChild==null){
						node.maxRightDepth = 0;
						if(node.leftChild==null){
							node.maxDistance = 0;
						}else{
							node.maxDistance = Math.max(node.leftChild.maxDistance, node.maxLeftDepth);
						}
						System.out.println(1+" "+node.value+" "+node.maxLeftDepth+" "+node.maxRightDepth);
						if(!s.isEmpty())
							node = s.peek().rightChild;
						else
							break;
					}else if(node.rightChild.maxLeftDepth>=0 && node.rightChild.maxRightDepth>=0){
						node.maxRightDepth = Math.max(node.rightChild.maxLeftDepth, node.rightChild.maxRightDepth)+1;
						if(node.leftChild==null){
							node.maxDistance = Math.max(node.rightChild.maxDistance, node.maxRightDepth);
						}else{
							node.maxDistance = Math.max(node.leftChild.maxDistance, 
									Math.max(node.rightChild.maxDistance, node.maxLeftDepth+node.maxRightDepth));
						}
						System.out.println(2+" "+node.value+" "+node.maxLeftDepth+" "+node.maxRightDepth);
						if(!s.isEmpty())
							node = s.peek().rightChild;
						else
							break;
					}else{
						s.push(node);
						node = node.rightChild;
					}
				}
			}
		}
		
		return root.maxDistance;
	}
	
	public static void print(TreeNode node){
		Queue<TreeNode> q = new LinkedList<TreeNode>();
		List<TreeNode> list = new ArrayList<TreeNode>();
		q.offer(node);
		while(!q.isEmpty()){
			list.clear();
			while(!q.isEmpty()){
				list.add(q.poll());
			}
			for(int i=0; i<list.size(); i++){
				System.out.print(list.get(i).value+" ");
				if(list.get(i).leftChild!=null){
					q.offer(list.get(i).leftChild);
				}
				if(list.get(i).rightChild!=null){
					q.offer(list.get(i).rightChild);
				}
				
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BinaryTree bt = new BinaryTree();
		//int[] pre = {1,2,4,7,3,5,6,8};
		//int[] in = {4,7,2,1,5,3,8,6};
		//TreeNode root = bt.orderPreIn(pre, in);
		//print(root);
		
		TreeNode n1 = new TreeNode(1);
		TreeNode n2 = new TreeNode(2);
		TreeNode n3 = new TreeNode(3);
		TreeNode n4 = new TreeNode(4);
		TreeNode n5 = new TreeNode(5);
		TreeNode n6 = new TreeNode(6);
		TreeNode n7 = new TreeNode(7);
		TreeNode n8 = new TreeNode(8);
		TreeNode n9 = new TreeNode(9);
		TreeNode n10 = new TreeNode(10);
		n1.leftChild = n2;
		n2.leftChild = n3;
		n3.leftChild = n4;
		n4.leftChild = n5;
		n5.leftChild = n6;
		n6.rightChild = n7;
		n7.rightChild = n8;
		n8.rightChild = n9;
		n9.rightChild = n10;
		//System.out.println(bt.hasSubtree(n1, n8));
		//bt.isBalancedTree(n1);
		//TreeNode t = new TreeNode();
		//System.out.println(t.value);
		//System.out.println(t.leftChild);
		System.out.println(bt.maxDistanceOfNodesIteration(n1));

	}

}

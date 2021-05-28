import java.io.FileNotFoundException;
import java.io.IOException;

public class TreeWalker extends IOTree {

	// it inherits root and size from LinkedBinaryTree vis IOTree;
	Position current;

	/************************************************************************************************/
	/** Constructors **/
	/** @param () ==> use default tree description **/
	/** (String toParse) ==> Tree built from this parsing **/
	/**                                                                                            **/
	/************************************************************************************************/

	public TreeWalker() throws IOException {
		this("((10 20 30) 40 (null 50 (60 70 80)))"); // The TreeWalker constructor with use default tree description
	}

	public TreeWalker(String toParse) {
		super(toParse);
		makeCurrent(root());
	}

	/************************************************************************************************
	 * root, parent, leftChild, and rightChild return the state position of given
	 * position. * - They create it if it is not there. *
	 ************************************************************************************************/

	public Position root(Position position) {
		if (super.root() != null)
			return super.root();
		else // Create a new root
		{
			Element element = new Element('r', 100, "new root", 'x');
			BTNode newroot = new BTNode(element, null, null, null);
			setRoot(newroot);
			return newroot;
		}
	}

	public Position parent(Position position) throws InvalidPositionException {
		if (!isRoot(position) && super.parent(position) != null) {
			return super.parent(position);
		} else if (isRoot(position)) { // Create a new root with current node as its left child.
			Element element = new Element('r', 10, "new root", 'x');
			BTNode node = (BTNode) position;
			BTNode newNode = new BTNode(element, node, null, null);
			node.setParent(newNode);
			setRoot(newNode);
			return set(newNode);
		} else {
			throw new InvalidPositionException("position has no parent.");
		}
	}

	public Position leftChild(Position position) {
		if (super.leftChild(position) != null) {
			return super.leftChild(position);
		} else { // Create a new left child for the current node.
			Element element = new Element('l', 20, "new left", 'x');
			BTNode node = (BTNode) position;
			BTNode newNode = new BTNode(element, null, null, node);
			node.setLeft(newNode);
			return set(newNode);
		}
	}

	public Position rightChild(Position position) {
		if (super.rightChild(position) != null) {
			return super.rightChild(position);
		} else { // Create a new right child for the current node.
			Element element = new Element('l', 20, "new right", 'x');
			BTNode node = (BTNode) position;
			BTNode newNode = new BTNode(element, null, null, node);
			node.setRight(newNode);
			return set(newNode);
		}
	}

	/************************************************************************************************
	 * rotateR rotates a node raising its left child to its position and lowering
	 * itself to the right * rotateL rotateR does the reverse *
	 ************************************************************************************************/

	public Position rotateR(Position position) {
		if (super.leftChild(position) == null) {
			System.out.println("No left child");
			return (position);
		}
		BTNode parent = (BTNode) super.parent(position);
		BTNode node = (BTNode) position;
		BTNode child = (BTNode) super.leftChild(position);
		BTNode tree2 = (BTNode) super.rightChild((Position) child);
		if (parent != null && parent.getLeft() == node)
			parent.setLeft(child);
		if (parent != null && parent.getRight() == node)
			parent.setRight(child);
		if (node == root())
			setRoot(child);
		child.setParent(parent);
		child.setRight(node);
		node.setParent(child);
		node.setLeft(tree2);
		if (tree2 != null)
			tree2.setParent(node);
		return (Position) child;
	}

	public Position rotateL(Position position) {
		if (super.rightChild(position) == null) {
			System.out.println("No right child");
			return (position);
		}
		BTNode parent = (BTNode) super.parent(position);
		BTNode node = (BTNode) position;
		BTNode child = (BTNode) super.rightChild(position);
		BTNode tree2 = (BTNode) super.leftChild((Position) child);
		if (parent != null && parent.getLeft() == node)
			parent.setLeft(child);
		if (parent != null && parent.getRight() == node)
			parent.setRight(child);
		if (node == root())
			setRoot(child);
		child.setParent(parent);
		child.setLeft(node);
		node.setParent(child);
		node.setRight(tree2);
		if (tree2 != null)
			tree2.setParent(node);
		return (Position) child;
	}

	/************************************************************************************************
	 * first, last, next, previous return this position according to the infix
	 * traversal order. * - The last two create it if it is not there. *
	 ************************************************************************************************/

	public Position first(Position position) {
		position = root();
		while (super.leftChild(position) != null)
			position = super.leftChild(position);
		return position;
	}

	public Position last(Position position) {
		position = root();
		while (super.rightChild(position) != null)
			position = super.rightChild(position);
		return position;
	}

	public Position next(Position position) {
		Position position1 = position;
		if (super.rightChild(position) != null) // go right left left left ...
		{
			position = super.rightChild(position);
			while (super.leftChild(position) != null)
				position = super.leftChild(position);
		} else {
			while (super.parent(position) != null && super.rightChild(super.parent(position)) == position) // position
																											// is a
																											// right
																											// child
				position = super.parent(position);
			if (super.parent(position) != null)
				position = super.parent(position);
			else // position1 is the last
				position = set(rightChild(position1)); // Note super not used. Hence, it creates the node.
		}
		return position;
	}

	public Position previous(Position position) {
		Position position1 = position;
		if (super.leftChild(position) != null) // go left right right right ...
		{
			position = super.leftChild(position);
			while (super.rightChild(position) != null)
				position = super.rightChild(position);
		} else {
			while (super.parent(position) != null && super.leftChild(super.parent(position)) == position) // position is
																											// a left
																											// child
				position = super.parent(position);
			if (super.parent(position) != null)
				position = super.parent(position);
			else // position1 is the last
				position = set(leftChild(position1)); // Note super not used. Hence, it creates the node.
		}
		return position;
	}

	/************************************************************************************************
	 * set sets the value of a position to be an integer within its binary search
	 * tree order * (set returns the same position to allow position =
	 * set(leftChild(position1)) *
	 ************************************************************************************************/

	public Position set(Position position) {
		int x;
		if (position == first(position) && position == last(position))
			x = value(position);
		else if (position == first(position))
			x = value(next(position)) - 10;
		else if (position == last(position))
			x = value(previous(position)) + 10;
		else
			x = (int) (value(previous(position)) + value(next(position))) / 2;
		setValue(position, x);
		return (position);
	}

	/*************************************************************************************************
	 * insert inserts a new node after the current node according to the tree's
	 * infix Traversal order,* i.e. go right and then left left left and put the new
	 * node there. *
	 ************************************************************************************************/

	public Position insert(Position position) {
		if (super.rightChild(position) == null)
			return set(rightChild(position)); // Note super not used. Hence, it creates the node.
		else {
			position = next(position);
			return set(leftChild(position)); // Note super not used. Hence, it creates the node.
		}
	}

	/*************************************************************************************************
	 * deletes the current node * If no right child use deleteNoRight, if no left
	 * use deleteNoLeft * else move contents of next to current and delete next *
	 ************************************************************************************************/

	/* Deletes current when no right child */
	/* Parent adopts left child */

	private Position deleteNoRight(Position position) {
		if (super.rightChild(position) == null) {
			BTNode parent = (BTNode) super.parent(position);
			BTNode node = (BTNode) position;
			BTNode child = (BTNode) super.leftChild(position);
			if (parent != null && parent.getLeft() == node)
				parent.setLeft(child);
			if (parent != null && parent.getRight() == node)
				parent.setRight(child);
			if (node == root())
				setRoot(child);
			if (child != null) {
				child.setParent(parent);
				position = child;
			} else
				position = parent;
			return position;
		} else {
			System.out.println("Panic: Is right child");
			return null;
		}
	}

	/* Deletes current when no left child */
	/* Parent adopts right child */

	private Position deleteNoLeft(Position position) {
		if (super.leftChild(position) == null) {
			BTNode parent = (BTNode) super.parent(position);
			BTNode node = (BTNode) position;
			BTNode child = (BTNode) super.rightChild(position);
			if (parent != null && parent.getLeft() == node)
				parent.setLeft(child);
			if (parent != null && parent.getRight() == node)
				parent.setRight(child);
			if (node == root())
				setRoot(child);
			if (child != null) {
				child.setParent(parent);
				position = child;
			} else
				position = parent;
			return position;
		} else {
			System.out.println("Panic: Is left child");
			return null;
		}
	}

	public Position delete(Position position) {
		if (super.rightChild(position) == null)
			return deleteNoRight(position);
		else if (super.leftChild(position) == null)
			return deleteNoLeft(position);
		else {
			BTNode node = (BTNode) position;
			BTNode toDelete = (BTNode) next(position);
			node.setElement(toDelete.element());
			deleteNoLeft(toDelete);
			return position;
		}
	}

	public int evaluate(int x, int y, int z) {
		System.out.println("Test: evaluate");
		System.out.println(recueval((BTNode) super.root(), x, y, z));
		return recueval((BTNode) super.root(), x, y, z);
	}

	private int recueval(BTNode tree, int x, int y, int z) {
		Element element = (Element) tree.element();

		if (element.IsNumb() == true) {
			return element.x;
		} else if (element.c == 'x') {
			return x;
		} else if (element.c == 'y') {
			return y;
		} else if (element.c == 'z') {
			return z;
		} else if (element.c == '+') {
			return recueval(tree.getLeft(), x, y, z) + recueval(tree.getRight(), x, y, z);
		} else if (element.c == '-') {
			return recueval(tree.getLeft(), x, y, z) - recueval(tree.getRight(), x, y, z);
		} else if (element.c == '*') {
			return recueval(tree.getLeft(), x, y, z) * recueval(tree.getRight(), x, y, z);
		} else if (element.c == '/') {
			return recueval(tree.getLeft(), x, y, z) / recueval(tree.getRight(), x, y, z);
		} else {
			System.out.println("Error: no proper operation set.");
			return 0;
		}
	}

	public void differentiate() {
		System.out.println("Test: differentiate");
		recudiff((BTNode) super.root(), 'x');

	}

	private BTNode recudiff(BTNode tree, char c) {

		Element element = (Element) tree.element();
		if (element.c == c) {
			Element element2 = new Element('r', 1, "new value", 'x');
			tree.setElement(element2);
			return tree;
		} else if (element.IsNumb() == true || (element.c >= 'a' && element.c <= 'z') == true) {
			Element element2 = new Element('r', 0, "new value", 'x');
			tree.setElement(element2);
			return tree;
		}
		BTNode g = copy(tree.getLeft());
		BTNode h = copy(tree.getRight());
		BTNode h2 = copy(tree.getRight());
		BTNode h3 = copy(tree.getRight());
		BTNode gd = (BTNode) recudiff(tree.getLeft(), c);
		BTNode hd = (BTNode) recudiff(tree.getRight(), c);


		if (element.c == '+') {
			tree.setLeft(gd);
			tree.setRight(hd);
			return tree;
		} else if (element.c == '-') {
			tree.setLeft(gd);
			tree.setRight(hd);
			return tree;
		} else if (element.c == '*') {
			Element times = new Element('*', 0, "new symbol", 'c');
			Element times2 = new Element('*', 0, "new symbol", 'c');
			Element plus = new Element('+', 0, "new symbol", 'c');
			BTNode left = new BTNode(times, gd, h, tree);
			BTNode right = new BTNode(times2, g, hd, tree);
			gd.setParent(left);
			h.setParent(left);
			g.setParent(right);
			hd.setParent(right);
			tree.setLeft(left);
			tree.setRight(right);
			tree.setElement(plus);

			return tree;
		} else if (element.c == '/') {
			Element div = new Element('/', 0, "new symbol", 'c');
			Element minus = new Element('-', 0, "new symbol", 'c');
			Element times = new Element('*', 0, "new symbol", 'c');
			Element times2 = new Element('*', 0, "new symbol", 'c');
			Element times3 = new Element('*', 0, "new symbol", 'c');
			BTNode subLeft = new BTNode(times, gd, h, null);
			BTNode subRight = new BTNode(times2, g, hd, null);
			BTNode left = new BTNode(minus, subLeft, subRight, tree);
			BTNode right = new BTNode(times3, h2, h3, tree);

			subLeft.setParent(left);
			subRight.setParent(left);
			gd.setParent(subLeft);
			h.setParent(subLeft);
			h2.setParent(right);
			h3.setParent(right);
			g.setParent(subRight);
			hd.setParent(subRight);

			tree.setLeft(left);
			tree.setRight(right);
			tree.setElement(div);

			return tree;
		}

		return tree;
	}

	private BTNode copy(BTNode tree) {

		if (tree == null) {
			return null;
		} else {
			BTNode treeCopy = new BTNode();
			treeCopy.setElement(tree.element());
			treeCopy.setLeft(copy(tree.getLeft()));
			treeCopy.setRight(copy(tree.getRight()));
			return treeCopy;

		}
	}

	public void simplify() {
		System.out.println("Test: simplify");

		recusimp((BTNode) super.root());
	}

	private BTNode recusimp(BTNode tree) {

		Element element = (Element) tree.element();

		if (element.IsNumb() == true) {
			return copy(tree);
		}

		else if (element.c == 'x' || element.c == 'y' || element.c == 'z') {
			return copy(tree);
		}

		else {

			BTNode g = recusimp(tree.getLeft());
			BTNode h = recusimp(tree.getRight());
			Element ge = (Element) g.element();
			Element he = (Element) h.element();

			if (ge.IsNumb() == true && he.IsNumb() == true) {
				int x;
				if (element.c == '*') {
					x = ge.x * he.x;
					Element xe = new Element('r', x, "new elem", 'x');
					tree.setElement(xe);
					delete(tree.getLeft());
					delete(tree.getRight());
					return tree;
				}

				else if (element.c == '/') {
					x = ge.x / he.x;
					Element xe = new Element('r', x, "new elem", 'x');
					tree.setElement(xe);
					delete(tree.getLeft());
					delete(tree.getRight());
					return tree;
				}

				else if (element.c == '+') {
					x = ge.x + he.x;
					Element xe = new Element('r', x, "new elem", 'x');
					tree.setElement(xe);
					delete(tree.getLeft());
					delete(tree.getRight());
					return tree;
				}

				else if (element.c == '-') {
					x = ge.x - he.x;
					Element xe = new Element('r', x, "new elem", 'x');
					tree.setElement(xe);
					delete(tree.getLeft());
					delete(tree.getRight());
					return tree;
				} else {
					System.out.println("Not a proper operation.");
					return tree;
				}
			}

			else if (element.c == '*') {

				if (ge.IsNumb() == true) {
					if (ge.x == 1) {
						tree.setLeft(null);
						return (BTNode) delete(tree);
					} else if (ge.x == 0) {
						Element zero = new Element('r', 0, "zero", 'x');
						tree.setElement(zero);
						tree.setLeft(null);
						tree.setRight(null);
						return tree;
					}
				}

				else if (he.IsNumb() == true) {
					if (he.x == 1) {
						tree.setRight(null);
						return (BTNode) delete(tree);
					} else if (he.x == 0) {
						Element zero = new Element('r', 0, "zero", 'x');
						tree.setElement(zero);
						tree.setLeft(null);
						tree.setRight(null);
						return tree;
					}
				}

				else {
					return tree;
				}
			}

			else if (element.c == '/') {

				if (ge.IsNumb() == true) {
					if (ge.x == 0) {
						Element zero = new Element('r', 0, "zero", 'x');
						tree.setElement(zero);
						tree.setLeft(null);
						tree.setRight(null);
						return tree;
					} else {
						return tree;
					}
				}

				else if (he.IsNumb() == true) {
					if (he.x == 1) {
						tree.setRight(null);
						return (BTNode) delete(tree);
					} else if (he.x == 0) {
						double i = Double.POSITIVE_INFINITY;
						Element inf = new Element('r', (int) i, "inf", 'x');
						tree.setElement(inf);
						tree.setLeft(null);
						tree.setRight(null);
						return tree;
					}
				} else if (ge.c == he.c) {
					Element one = new Element('r', 1, "one", 'x');
					tree.setElement(one);
					tree.setLeft(null);
					tree.setRight(null);
					return tree;
				} else {
					return tree;
				}
			}

			else if (element.c == '-') {

				if (he.IsNumb() == true) {
					if (he.x == 0) {
						tree.setRight(null);
						return (BTNode) delete(tree);
					}
				} else if (ge.c == he.c) {
					Element zero = new Element('r', 0, "zero", 'x');
					tree.setElement(zero);
					tree.setLeft(null);
					tree.setRight(null);
					return tree;
				} else {
					return tree;
				}
			}

			else if (element.c == '+') {

				if (ge.IsNumb() == true) {
					if (ge.x == 0) {
						tree.setLeft(null);
						return (BTNode) delete(tree);
					}
				}

				else if (he.IsNumb() == true) {
					if (he.x == 0) {
						tree.setRight(null);
						return (BTNode) delete(tree);
					}
				} else {
					return tree;
				}
			}

		}

		return tree;
	}

}
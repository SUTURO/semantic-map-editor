package com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Node for a tree structure.
 *
 * @param <T> the type of the data stored in the node
 */
@EqualsAndHashCode
@ToString
public class Node<T> implements Iterable<Node<T>> {

    @Getter
    private final T data;

    private Node<T> parent;

    @Getter
    private final List<Node<T>> children = new LinkedList<>();

    public Node(@NonNull T data) {
        this.data = data;
    }

    public Optional<Node<T>> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Creates a new child node with the given data and adds it to the children of this node.
     *
     * @param childData the data of the child node
     * @return the child node
     */
    public Node<T> addChild(T childData) {
        Node<T> childNode = new Node<>(childData);
        return this.addChild(childNode);
    }

    /**
     * Adds the given child node to the children of this node.
     *
     * @param childNode the child node to add
     * @return the child node
     */
    public Node<T> addChild(Node<T> childNode) {
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    /**
     * Removes the given child node from the children of this node.
     *
     * @param child the child node to remove
     * @return true, if the child node was removed
     */
    public boolean removeChild(Node<T> child) {

        boolean removed = this.children.remove(child);

        if (removed)
            child.parent = null;

        return removed;
    }

    /**
     * Checks the level of this node in the tree.
     *
     * <p>
     * The root node has level 0.
     * </p>
     *
     * @return the level of this node in the tree
     */
    public int getLevel() {
        if (this.isRoot())
            return 0;
        else
            return parent.getLevel() + 1;
    }

    /**
     * Checks whether this node is the root node.
     *
     * @return true, if this node has no parent
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Checks whether this node is a leaf node.
     *
     * @return true, if this node has no children
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new NodeIterator<>(this);
    }

    private static class NodeIterator<T> implements Iterator<Node<T>> {

        enum ProcessStages {
            ProcessParent, ProcessChildCurNode, ProcessChildSubNode
        }

        private final Node<T> node;

        private ProcessStages doNext;
        private Node<T> next;
        private final Iterator<Node<T>> childrenCurNodeIter;
        private Iterator<Node<T>> childrenSubNodeIter;

        public NodeIterator(@NonNull Node<T> node) {
            this.node = node;
            this.doNext = ProcessStages.ProcessParent;
            this.childrenCurNodeIter = node.children.iterator();
        }

        @Override
        public boolean hasNext() {

            if (this.doNext == ProcessStages.ProcessParent) {
                this.next = this.node;
                this.doNext = ProcessStages.ProcessChildCurNode;
                return true;
            }

            if (this.doNext == ProcessStages.ProcessChildCurNode) {
                if (childrenCurNodeIter.hasNext()) {
                    Node<T> childDirect = childrenCurNodeIter.next();
                    childrenSubNodeIter = childDirect.iterator();
                    this.doNext = ProcessStages.ProcessChildSubNode;
                    return hasNext();
                } else {
                    this.doNext = null;
                    return false;
                }
            }

            if (this.doNext == ProcessStages.ProcessChildSubNode) {
                if (childrenSubNodeIter.hasNext()) {
                    this.next = childrenSubNodeIter.next();
                    return true;
                } else {
                    this.next = null;
                    this.doNext = ProcessStages.ProcessChildCurNode;
                    return hasNext();
                }
            }

            return false;
        }

        @Override
        public Node<T> next() {
            return this.next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}

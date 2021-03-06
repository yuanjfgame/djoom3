package neo.idlib.containers;

import neo.idlib.Lib.idLib;

/**
 *
 */
public class Hierarchy {

    /*
     ==============================================================================

     idHierarchy

     ==============================================================================
     */
    public static class idHierarchy<type> {

        private idHierarchy parent;
        private idHierarchy sibling;
        private idHierarchy child;
        private type owner;
        //
        //

        public idHierarchy() {
            owner = null;
            parent = null;
            sibling = null;
            child = null;
        }
//public						~idHierarchy();
//	

        /*
         ================
         idHierarchy<type>::SetOwner

         Sets the object that this node is associated with.
         ================
         */
        public void SetOwner(type object) {
            owner = object;
        }

        /*
         ================
         idHierarchy<type>::Owner

         Gets the object that is associated with this node.
         ================
         */
        public type Owner() {
            return owner;
        }

        /*
         ================
         idHierarchy<type>::ParentTo

         Makes the given node the parent.
         ================
         */
        public void ParentTo(idHierarchy node) {
            RemoveFromParent();

            parent = node;
            sibling = node.child;
            node.child = this;
        }

        /*
         ================
         idHierarchy<type>::MakeSiblingAfter

         Makes the given node a sibling after the passed in node.
         ================
         */
        public void MakeSiblingAfter(idHierarchy node) {
            RemoveFromParent();
            parent = node.parent;
            sibling = node.sibling;
            node.sibling = this;
        }

        public boolean ParentedBy(final idHierarchy node) {
            if (parent == node) {
                return true;
            } else if (parent != null) {
                return parent.ParentedBy(node);
            }
            return false;
        }

        public void RemoveFromParent() {
            idHierarchy<type> prev;

            if (parent != null) {
                prev = GetPriorSiblingNode();
                if (prev != null) {
                    prev.sibling = sibling;
                } else {
                    parent.child = sibling;
                }
            }

            parent = null;
            sibling = null;
        }

        /*
         ================
         idHierarchy<type>::RemoveFromHierarchy

         Removes the node from the hierarchy and adds it's children to the parent.
         ================
         */
        public void RemoveFromHierarchy() {
            idHierarchy<type> parentNode;
            idHierarchy<type> node;

            parentNode = parent;
            RemoveFromParent();

            if (parentNode != null) {
                while (child != null) {
                    node = child;
                    node.RemoveFromParent();
                    node.ParentTo(parentNode);
                }
            } else {
                while (child != null) {
                    child.RemoveFromParent();
                }
            }
        }

        // parent of this node
        public type GetParent() {
            if (parent != null) {
                return (type) parent.owner;
            }
            return null;
        }

        // first child of this node
        public type GetChild() {
            if (child != null) {
                return (type) child.owner;
            }
            return null;
        }

        // next node with the same parent
        public type GetSibling() {
            if (sibling != null) {
                return (type) sibling.owner;
            }
            return null;
        }

        /*
         ================
         idHierarchy<type>::GetPriorSiblingNode

         Returns NULL if no parent, or if it is the first child.
         ================
         */
        public type GetPriorSibling() {        // previous node with the same parent
            if (null == parent || (parent.child == this)) {
                return null;
            }

            idHierarchy<type> prev;
            idHierarchy<type> node;

            node = parent.child;
            prev = null;
            while ((node != this) && (node != null)) {
                prev = node;
                node = node.sibling;
            }

            if (node != this) {
                idLib.Error("idHierarchy::GetPriorSibling: could not find node in parent's list of children");
            }

            return (type) prev;
        }

        /*
         ================
         idHierarchy<type>::GetNext

         Goes through all nodes of the hierarchy.
         ================
         */
        public type GetNext() {			// goes through all nodes of the hierarchy
            idHierarchy<type> node;

            if (child != null) {
                return (type) child.owner;
            } else {
                node = this;
                while (node != null && null == node.sibling) {
                    node = node.parent;
                }
                if (node != null) {
                    return (type) node.sibling.owner;
                } else {
                    return null;
                }
            }
        }

        /*
         ================
         idHierarchy<type>::GetNextLeaf

         Goes through all leaf nodes of the hierarchy.
         ================
         */
        public type GetNextLeaf() {		// goes through all leaf nodes of the hierarchy
            idHierarchy<type> node;

            if (child != null) {
                node = child;
                while (node.child != null) {
                    node = node.child;
                }
                return node.owner;
            } else {
                node = this;
                while (node != null && null == node.sibling) {
                    node = node.parent;
                }
                if (node != null) {
                    node = node.sibling;
                    while (node.child != null) {
                        node = node.child;
                    }
                    return node.owner;
                } else {
                    return null;
                }
            }
        }

        /*
         ================
         idHierarchy<type>::GetPriorSibling

         Returns NULL if no parent, or if it is the first child.
         ================
         */
        private idHierarchy<type> GetPriorSiblingNode() {// previous node with the same parent
            idHierarchy<type> prior;

            prior = GetPriorSiblingNode();
            if (prior != null) {
                return (idHierarchy<type>) prior.owner;
            }

            return null;
        }
    };
}

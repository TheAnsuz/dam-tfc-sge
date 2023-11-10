package dev.amrv.sge;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
/*
 * w w w. jav a2 s . co  m
 */
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class TreeTest extends JFrame {

    public TreeTest() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TreeNode root = getNodes();
        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
        tree.setRootVisible(false);

        JButton add = new JButton("add new");
        add.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                    .getLastSelectedPathComponent();
            if (selectedNode == null) {
                return;
            }
            MyObject obj = (MyObject) selectedNode.getUserObject();
            MyObject newChild = new MyObject(obj.name + "-"
                    + (obj.childs.size() + 1));
            obj.childs.add(newChild);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newChild);
            model.insertNodeInto(newNode, selectedNode,
                    selectedNode.getChildCount());
            TreeNode[] nodes = model.getPathToRoot(newNode);
            TreePath path = new TreePath(nodes);
            tree.scrollPathToVisible(path);
        });

        JButton print = new JButton("print childs");
        print.addActionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                    .getLastSelectedPathComponent();
            if (selectedNode == null) {
                return;
            }
            MyObject obj = (MyObject) selectedNode.getUserObject();
            System.out.println(obj.childs);
        });
        JPanel btns = new JPanel();
        btns.add(add);
        btns.add(print);

        add(new JScrollPane(tree));
        add(btns, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private TreeNode getNodes() {
        MyObject obj1 = new MyObject("1");
        MyObject obj2 = new MyObject("1-1");

        obj1.childs.add(obj2);
        obj2.childs.add(new MyObject("2-1"));
        obj2.childs.add(new MyObject("2-2"));

        obj1.childs.add(new MyObject("1-2"));
        obj1.childs.add(new MyObject("1-3"));

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        construct(obj1, root);
        return root;
    }

    private void construct(MyObject obj1, DefaultMutableTreeNode root) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(obj1);
        root.add(node);
        for (MyObject o : obj1.childs) {
            construct(o, node);
        }
    }

    public static void main(String... strings) {
        new TreeTest();
    }
}

class MyObject {

    public String name;
    public List<MyObject> childs = new ArrayList<>();

    public MyObject(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

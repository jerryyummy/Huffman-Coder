public class Node implements Comparable<Node>{
    public String chars = "";//节点保存的字符
    public int frequence = 0;//权重
    public Node parent;//父节点
    public Node leftNode;//左孩子
    public Node rightNode;//右孩子

    @Override
    public int compareTo(Node n) {
        return frequence - n.frequence;
    }//返回权重之差

    public boolean isLeaf() {
        return chars.length() == 1;
    }//判断是否是叶子节点

    public boolean isRoot() {
        return parent == null;
    }//判断是否是根节点

    public boolean isLeftChild() {
        return parent != null && this == parent.leftNode;
    }//判断是否是左孩子子节点

    public int getFrequence() {
        return frequence;
    }//获取权重

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }//设置权重

    public String getChars() {
        return chars;
    }//获取字符

    public void setChars(String chars) {
        this.chars = chars;
    }//设置字符

    public Node getParent() {
        return parent;
    }//获取父节点

    public void setParent(Node parent) {
        this.parent = parent;
    }//设置父节点

    public Node getLeftNode() {
        return leftNode;
    }//获取左孩子节点

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }//设置左孩子节点

    public Node getRightNode() {
        return rightNode;
    }//获取右孩子节点

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }//设置右孩子节点
}
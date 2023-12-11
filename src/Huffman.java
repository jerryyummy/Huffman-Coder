import javax.swing.plaf.basic.BasicTextAreaUI;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class Huffman {
    Vector<Byte> bits=new Vector<Byte>();
    public static Map<Character, Integer> statistics(char[] charArray) {//利用char数组创建map
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (char c : charArray) {//遍历char数组并创建键值对
            Character character = new Character(c);
            if (map.containsKey(character)) {//如果map中有该键则改键的权重加一
                map.put(character, map.get(character) + 1);
            } else {//没有该键则设置该键权重为1
                map.put(character, 1);
            }
        }
        return map;
    }
    private static Tree buildTree(Map<Character, Integer> statistics,
                                  List<Node> leafs) {//构建HUFFMAN树
        Character[] keys = statistics.keySet().toArray(new Character[0]);//读取map中的键

        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();//构建Node的优先级队列
        for (Character character : keys) {//遍历map中的键
            Node node = new Node();
            node.chars = character.toString();//将键读入哈夫曼NODE
            node.frequence = statistics.get(character);//读入权重
            priorityQueue.add(node);//将node插入优先级队列
            leafs.add(node);//将node传入叶子节点数组
        }

        int size = priorityQueue.size();
        for (int i = 1; i <= size - 1; i++) {
            Node node1 = priorityQueue.poll();//取出优先级队列中权值最小的节点，并删除
            Node node2 = priorityQueue.poll();//取出优先级队列中权值最小的节点，并删除

            Node sumNode = new Node();//用于将两节点相加
            sumNode.chars = node1.chars + node2.chars;
            sumNode.frequence = node1.frequence + node2.frequence;

            sumNode.leftNode = node1;//链接新节点的左叶子
            sumNode.rightNode = node2;//链接新节点的右叶子

            node1.parent = sumNode;
            node2.parent = sumNode;

            priorityQueue.add(sumNode);//相加的节点存入优先级队列
        }

        Tree tree = new Tree();
        tree.root = priorityQueue.poll();//最后一个优先级队列中节点是root节点
        return tree;
    }
    public static String encode(String originalStr,
                                Map<Character, Integer> statistics) {
        if (originalStr == null || originalStr.equals("")) {//防止文件为空
            return "";
        }

        char[] charArray = originalStr.toCharArray();
        List<Node> leafNodes = new ArrayList<Node>();
        buildTree(statistics, leafNodes);//构建树，将节点连接起来
        Map<Character, String> encodInfo = buildEncodingInfo(leafNodes);//获取每个叶子结点的huffman码并存入图中

        StringBuffer buffer = new StringBuffer();
        for (char c : charArray) {//将每个字符利用图中的huffman编码加密
            Character character = new Character(c);
            buffer.append(encodInfo.get(character));
        }

        return buffer.toString();
    }
    private static Map<Character, String> buildEncodingInfo(List<Node> leafNodes) {//用于给每个叶子节点构建huffma码
        Map<Character, String> codewords = new HashMap<Character, String>();//保存键值对，值是huffman编码
        for (Node leafNode : leafNodes) {
            Character character = new Character(leafNode.getChars().charAt(0));//获取键
            String codeword = "";
            Node currentNode = leafNode;//获取当前节点

            do {//用于给当前节点构建huffman码
                if (currentNode.isLeftChild()) {//如果是父节点左孩子哈夫曼编码加1
                    codeword = "0" + codeword;
                } else {
                    codeword = "1" + codeword;//如果是父节点有孩子哈夫曼编码加0
                }

                currentNode = currentNode.parent;
            } while (currentNode.parent != null);

            codewords.put(character, codeword);
        }

        return codewords;
    }
    public static String decode(String binaryStr,
                                Map<Character, Integer> statistics) {
        if (binaryStr == null || binaryStr.equals("")) {//防止文件为空
            return "";
        }

        char[] binaryCharArray = binaryStr.toCharArray();
        LinkedList<Character> binaryList = new LinkedList<Character>();//创建链表用于储存String二进制码
        int size = binaryCharArray.length;
        for (int i = 0; i < size; i++) {
            binaryList.addLast(new Character(binaryCharArray[i]));//储存String二进制码
        }

        List<Node> leafNodes = new ArrayList<Node>();//保存叶子节点
        Tree tree = buildTree(statistics, leafNodes);//用权重构建树

        StringBuffer buffer = new StringBuffer();

        while (binaryList.size() > 0) {//利用循环来找到前几位哈发满码对应的字符
            Node node = tree.root;

            do {
                Character c = binaryList.removeFirst();//获取一个二进制码以此从根节点开始找到叶子节点
                if (c.charValue() == '0') {
                    node = node.leftNode;
                } else {
                    node = node.rightNode;
                }
            } while (!node.isLeaf());

            buffer.append(node.chars);//保存对应字符
        }

        return buffer.toString();
    }
    public static void main(String[] args) throws Exception {
//        String oriStr="ssadksadwq d sad qwed sad d asd asd\n" +
//                "daa";
//        Map<Character, Integer> statistics = statistics(oriStr.toCharArray());
//        String encodedBinariStr = encode(oriStr,statistics);
//        String decodedStr = decode(encodedBinariStr,statistics);
//       savemap(statistics);
//        savezip(encodedBinariStr,"02.txt");
//        String oriStr=readfile("00.txt");
//        Map<Character, Integer> statistics = statistics(oriStr.toCharArray());
//        String encodedBinariStr = encode(oriStr, statistics);
//        savemap(statistics,"123.txt");
//        savezip(encodedBinariStr,"01.txt");
//        String decodedStr = decode(readzip("01.txt"), readmap("123.txt"));
//        writefile("03.txt",decodedStr);
//        System.out.print("\t" + statistics);
//        System.out.print("\t" + readmap("123.txt"));
//        System.out.println("Original sstring: " + oriStr);
//        System.out.println("Huffman encoed binary string: " + encodedBinariStr);
//        System.out.println("decoded string from binariy string: " + decodedStr);
//          savezip("1111111111111101","04.txt");
    }
    public static void savemap(Map<Character, Integer> map,String filename) throws Exception {//保存map键值对
        try {
            String line = System.getProperty("line.separator");//换行符,功能和"\n"是一致的,但是此种写法屏蔽了 Windows和Linux的区别 ，更保险一些.
            StringBuffer str = new StringBuffer();
            FileWriter fw = new FileWriter("map"+filename, false);
            Set set = map.entrySet();
            Iterator iter = set.iterator();//遍历器
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();//获取键值对
                str.append(entry.getKey()+"$"+entry.getValue()).append(line);//将键值对保存到str并用'$'隔开
            }
            fw.write(str.toString());//将str写入文件
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Map<Character, Integer> readmap(String filename)  {//读取map键值对
        Map<Character, Integer> map = new HashMap<Character, Integer>();//创建map用于储存读入的键值对
        BufferedReader reader =
                null;
        try {
            reader = new BufferedReader(new FileReader("map"+filename));

        String line =  reader.readLine();
        while(line != null) {
            if(line.equals("")){//判断语句用于判读是否读到'\n'和其对应的键值对
                line =  reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line, "$");//因为键值对是用'$'隔开的，所以用其区分键值对
                map.put('\n',Integer.valueOf(tokenizer.nextToken()));//将读入的键值对写入到map中
                line=reader.readLine();
            }
            else{
                StringTokenizer tokenizer = new StringTokenizer(line, "$");//因为键值对是用'$'隔开的，所以用其区分键值对
                map.put(tokenizer.nextToken().charAt(0),Integer.valueOf(tokenizer.nextToken()));//将读入的键值对写入到map中
                line=reader.readLine();}
        }reader.close();
        }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        catch (IOException e) {
            e.printStackTrace();
        }
        return map;

    }
    public static char byteToChar(byte[] b) {//byte[2]转变变为char
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }
    public static byte[] charToByte(char c){//将char还原为byte
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }
    private static byte setBit(byte b,int index){//用于将byte指定下标元素置为1
        b |= (1 << ((index) ^ 7));
        return b ;
    }
    private static byte getbit(byte b,int index){//用于获取byte指定下标
        byte c=0;
        c |= ((b << (index+24))>>31);
        return c ;
    }
    public static String savezip(String s){//保存压缩文件
        byte[] bits= new byte[2];
        int index=0;
        String zipcode="";
        for(char c: s.toCharArray()){
            if(c=='1'){
                bits[index/8]=   setBit(bits[index/8],index%8);
            }
            if(index==15){
                zipcode=zipcode+ byteToChar(bits);//将byte转化为char存储再zipcode中
                index=-1;
                bits[0]=0;
                bits[1]=0;
            }
            index++;
        }
        zipcode=zipcode+'$';//用'$'来区分最后余下的字符，但是文本中有'$'会产生错误
        zipcode=zipcode+s.substring(s.length()-(index));//将余下哈夫曼字符存入zipcode中
       return zipcode;
    }
    public static String readzip(String c ){//读取压缩文件
//        String s="";
//        byte[] bits= new byte[2];
//        byte judge;
//        try{  //BufferedReader reader =
//            //new BufferedReader(new FileReader(file));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));//创建流，以utf-16形式读入
//            int line = reader.read();
//            String c="";
//            while(line!=-1&&(char) line!='$'){//读入压缩字符直到最后余下的哈夫曼字符
//                c=c+(char)line;
//                line = reader.read();
//            }
//            for(char i:c.toCharArray() ){
//                bits=charToByte(i);//将读入的字符还原为byte型
//                for(int j=0;j<16;j++){
//                    judge=getbit(bits[j/8],j%8);//将byte中每一位还原为字符串
//                    if(judge==-1){
//                        s=s+'1';
//                    }
//                    else s=s+'0';
//                }
//            }
//            line = reader.read();
//            while(line!=-1){
//                s=s+(char)line;//将余下的哈夫曼字符继续读入，到此所有哈夫曼码以还原
//                line = reader.read();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //s=s.substring(0,s.length()-2);
                String s="";
        byte[] bits= new byte[2];
        byte judge;
        int i=0;
        String n="";
        while(c.charAt(i)!='$'){
            n+=c.charAt(i);
            i++;
        }
        for(char p:n.toCharArray() ){
                bits=charToByte(p);//将读入的字符还原为byte型
                for(int j=0;j<16;j++){
                    judge=getbit(bits[j/8],j%8);//将byte中每一位还原为字符串
                    if(judge==-1){
                        s=s+'1';
                    }
                    else s=s+'0';
                }
            }
        for (int j = i+1; j <c.toCharArray().length ; j++) {
            s+=c.charAt(j);
        }
        s=s.substring(0,s.length()-1);
        return s;
    }
    public static String readfile(String file){//读取需要压缩的未压缩文件
        String s="";//用于保存读入的字符串
        try{  BufferedReader reader =
                new BufferedReader(new FileReader(file));
            String line = ""+reader.readLine();//读取一整行
            while(line!=null){
                s=s+line+'\n';//每一整行后加'\n'用于还原时候换行
                line=reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    public static void writefile(String filename,String content) throws IOException {//写未压缩或者解压文件
        PrintWriter fileOut =
                new PrintWriter(new FileWriter(filename));
        fileOut.println(content);//将内容写入文件
        fileOut.close();

    }
}

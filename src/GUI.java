import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GUI {
    static String filename="";
    public static void main(String[] args) throws Exception {
        final JFrame jf = new JFrame("数据结构huffman实验");
        jf.setSize(800, 600);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /*
         * 创建两个文本框
         */
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(1,2,10,10));//网格布局，放置两个文本框
        JTextArea inputText=new JTextArea();
        inputText.setEditable(false);//不可编辑
        inputText.setLineWrap(true);//默认换行
        inputText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane jScrollPane1=new JScrollPane(inputText);//添加滑动框,默认水平滑动框不显示

        JTextArea outputText=new JTextArea();
        outputText.setEditable(false);
        outputText.setLineWrap(true);//默认换行
        outputText.setForeground(Color.BLUE);//设置文字颜色为蓝色与输入文本框进行区分
        outputText.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane jScrollPane2=new JScrollPane(outputText);//添加滑动框

        panel.add(jScrollPane1);//容器添加两个文本框和相关的滑动文本框
        panel.add(jScrollPane2);
        /*
         * 创建一个菜单栏
         */
        JMenuBar menuBar = new JMenuBar();
        /*
         * 创建一级菜单
         */
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu aboutMenu = new JMenu("About");
        // 一级菜单添加到菜单栏
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(aboutMenu);

        /*
         * 创建 "文件" 一级菜单的子菜单
         */
        JMenuItem openFileItem = new JMenuItem("打开输入文件");
        JMenuItem saveFileItem = new JMenuItem("保存输出文件");
        // 子菜单添加到一级菜单
        fileMenu.add(openFileItem);
        fileMenu.addSeparator();       // 添加一条分割线
        fileMenu.add(saveFileItem);

        /*
         * 创建 "编辑" 一级菜单的子菜单
         */
        JMenuItem encodeItem = new JMenuItem("编码");
        JMenuItem decodeItem = new JMenuItem("解码");
        // 子菜单添加到一级菜单
        editMenu.add(encodeItem);
        editMenu.addSeparator();       // 添加一条分割线
        editMenu.add(decodeItem);
        /*
         * 创建 "关于" 一级菜单的子菜单
         */
        JMenuItem aboutItem = new JMenuItem("关于我们");
        // 子菜单添加到一级菜单
        aboutMenu.add(aboutItem);

        /*
         * 菜单项的点击/状态改变事件监听，事件监听可以直接设置在具体的子菜单上（这里只设置其中几个）
         */
        // 设置 "打开文件" 子菜单被点击的监听器
        openFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();//选择文件
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(jf);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String s2=file.toString();
                    for(char c :s2.toCharArray()){
                        if(c=='\\'){
                            filename+="\\\\";
                    }
                    else  filename+=c;
                    }
                    FileReader reader = null;//定义一个fileReader对象，用来初始化BufferedReader
                    try {
                        reader = new FileReader(file);
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
                    StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
                    String s = "";
                    while (true) {
                        try {
                            if (!((s =bReader.readLine()) != null))
                                break;
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }//逐行读取文件内容，不读取换行符和末尾的空格
                        sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
                    }
                    try {
                        bReader.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    String str = sb.toString();
                    inputText.setText(str);
                    try {
                        reader.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                } else {
                    inputText.setText("打开命令取消");
                }


            }
        });
        // 设置 "保存文件" 子菜单被点击的监听器
        saveFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Huffman text1=new Huffman();
                JFileChooser fileChooser = new JFileChooser();//选择文件
                fileChooser.setSelectedFile(new File("Huffman.txt"));//选择路径进行保存
                fileChooser.showSaveDialog(null);

                String filePath = fileChooser.getSelectedFile().toString();
                String string=inputText.getText();
                Map<Character, Integer> statistics =text1. statistics(string.toCharArray());
                String encodedBinariStr = text1. encode(string, statistics);
                try {
                    FileWriter writer = new FileWriter(filePath);
                    writer.append(text1.savezip(encodedBinariStr));//向新建文件写入数据
                    writer.flush();//文件缓冲
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    text1.savemap(statistics,".txt");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        // 设置 "编码" 子菜单被点击的监听器
        encodeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Huffman text1=new Huffman();
                String string=inputText.getText();
                Map<Character, Integer> statistics =text1. statistics(string.toCharArray());
                String encodedBinariStr = text1. encode(string, statistics);
                String line = System.getProperty("line.separator");//换行符,功能和"\n"是一致的,但是此种写法屏蔽了 Windows和Linux的区别 ，更保险一些.
                StringBuffer str = new StringBuffer();
                Set set = statistics.entrySet();
                Iterator iter = set.iterator();//遍历器
                while(iter.hasNext()){
                    Map.Entry entry = (Map.Entry)iter.next();//获取键值对
                    if(entry.getKey().equals('\n')){
                        str.append("回车"+":"+entry.getValue()).append(line);
                    }
                    else str.append(entry.getKey()+":"+entry.getValue()).append(line);//将键值对保存到str并用'$'隔开
                }
                outputText.setText(str.toString()+'\n'+encodedBinariStr);
            }
        });
        // 设置 "解码" 子菜单被点击的监听器
        decodeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Huffman text1=new Huffman();
                String decodedStr = text1.decode(text1.readzip(inputText.getText()),text1.readmap(".txt"));
                outputText.setText(decodedStr);
            }
        });
        // 设置 "关于" 子菜单被点击的监听器
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(jf,"Huffman编码解码工具，可以进行文本文件的编码与" +
                        "解码并保存\n可以手动选择打开文件和保存文件的路径\n文本框内不可编辑，只能显示文件内容","关于我们",JOptionPane.INFORMATION_MESSAGE);//展示对话框
            }
        });

        /*
         * 最后 把菜单栏设置到窗口
         */
        jf.setJMenuBar(menuBar);
        jf.add(panel);
        jf.setVisible(true);
    }

}

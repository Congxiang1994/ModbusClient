package com.congxiang.modbus.util;

import java.util.ArrayList;  
import java.util.List;  
  
/** 
 * ���Ľṹ 
 * @author John 
 * 
 */  
public class Node {  
    private String name;    //�ý������  
    private int layer = 0;  //�ý��㼶  
  
    private List<Node> childs = null; //����ý��ĺ���  
  
    public Node(String name){  
        this.name = name;  
    }  
    public Node(){  
    }  
      
    /** 
     * ����һ������ 
     * @param n Ҫ��Ϊ�������ӵĽ�� 
     */  
    public void add(Node n){  
        if(childs == null)  
            childs = new ArrayList<Node>();  
        n.setLayer(layer+1);  
        setChildLayout(n);  
        childs.add(n);  
    }  
      
    /** 
     * �ݹ����ú��ӵĲ㼶 
     * @param n 
     */  
    private void setChildLayout(Node n){  
        if(n.hasChild()){  
            List<Node> c = n.getChilds();  // ��ȡ���ڵ���ӽڵ�
            for(Node node : c){  
                node.setLayer(node.getLayer()+1);  
                setChildLayout(node);  
            }  
        }  
    }  
  
    /** 
     * ��ȡ����� 
     * @return ����� 
     */  
    public String getName() {  
        return name;  
    }  
  
    /** 
     * ���ý���� 
     * @param name ����� 
     */  
    public void setName(String name) {  
        this.name = name;  
    }     
  
    /** 
     * ��ȡ�ý��Ĳ㼶 
     * @return �ý��Ĳ㼶 
     */  
    public int getLayer() {  
        return layer;  
    }  
  
    /** 
     * ���øý��Ĳ㼶 
     * @param layer �ý��Ĳ㼶 
     */  
    public void setLayer(int layer) {  
        this.layer = layer;  
    }  
      
    /** 
     * ��ȡ�ý��ĺ��� 
     * @return ���к��ӽ�� 
     */  
    public List<Node> getChilds() {  
        return childs;  
    }  
  
    /** 
     * ����Ƿ���ں��� 
     * @return ���򷵻�true�����򷵻�false 
     */  
    public boolean hasChild(){  
        return childs == null ? false : true;  
    }  
      
    /** 
     * �ݹ��ӡ���еĽ�㣨�����ӽ�㣩 
     * @param n Ҫ��ӡ�ĸ���� 
     */  
    public void printAllNode(Node n){  
        System.out.println(n);  
        if(n.hasChild()){  
            List<Node> c = n.getChilds();  
            for(Node node : c){ 
            	System.out.print(n+":");
                printAllNode(node);  
            }  
        }  
    }  
      
    public String getAllNodeName(Node n){  
        String s = n.toString()+" ; ";  
        if(n.hasChild()){  // ������ӽڵ�
            List<Node> c = n.getChilds();  
            for(Node node : c){  // ���������ӽڵ�
                s+=getAllNodeName(node)+" ; ";  
            }  
        }  
        return s;  
    }  
      
    public String toString(){  
        return layer+"��: "+name;  
    }  
}  
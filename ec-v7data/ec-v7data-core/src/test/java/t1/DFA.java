package t1;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 徐良永
 * @Date   2011-10-13 上午9:23:43
 */
public class DFA {
	
	private static final Logger logger = LoggerFactory.getLogger(DFA.class);
	/**
	 * 根节点
	 */
	private TreeNode rootNode = new TreeNode();
	
	/**
	 * 关键词缓存
	 */
	private ByteBuffer keywordBuffer = ByteBuffer.allocate(1024);	
	
	/**
	 * 关键词编码
	 */
	private String charset = "GBK";

	/**
	 * 创建DFA
	 * @param keywordList
	 * @throws UnsupportedEncodingException 
	 */
	public void createKeywordTree(List<String> keywordList) throws UnsupportedEncodingException{
		
		for (String keyword : keywordList) {
			if(keyword == null) continue;
			keyword = keyword.trim();
			byte[] bytes = keyword.getBytes(charset);
			
			TreeNode tempNode = rootNode;
			//循环每个字节
			for (int i = 0; i < bytes.length; i++) {
				int index = bytes[i] & 0xff; //字符转换成数字
				TreeNode node = tempNode.getSubNode(index);
				
				if(node == null){ //没初始化
					node = new TreeNode();
					tempNode.setSubNode(index, node);
				}
				
				tempNode = node;
				
				if(i == bytes.length - 1){
					tempNode.setKeywordEnd(true);	 //关键词结束， 设置结束标志
					logger.debug("DFA:{}", keyword);
				}
			}//end for
		}//end for
		
	}
	
	/**
	 * 搜索关键字
	 */
	public String searchKeyword(String text) throws UnsupportedEncodingException{
		return searchKeyword(text.getBytes(charset));
	}
	
	/**
	 * 搜索关键字
	 */
	public String searchKeyword(byte[] bytes){
		StringBuilder words = new StringBuilder();
		
		if(bytes == null || bytes.length == 0){
			return words.toString();
		}
		
		TreeNode tempNode = rootNode;
		int rollback = 0;	//回滚数
		int position = 0; //当前比较的位置
		
		while (position < bytes.length) {
			int index = bytes[position] & 0xFF;
			keywordBuffer.put(bytes[position]);	//写关键词缓存
			tempNode = tempNode.getSubNode(index);
			
			//当前位置的匹配结束
			if(tempNode == null){ 
				position = position - rollback; //回退 并测试下一个字节
				rollback = 0;
				tempNode = rootNode;  	//状态机复位
				keywordBuffer.clear();	//清空
			}
			else if(tempNode.isKeywordEnd()){  //是结束点 记录关键词
				keywordBuffer.flip();
				String keyword = Charset.forName(charset).decode(keywordBuffer).toString();
				logger.debug("Find keyword:{}", keyword);
				keywordBuffer.limit(keywordBuffer.capacity());
				
				if( words.length() == 0 ) words.append(keyword);
				else words.append(":").append(keyword);
				
				rollback = 1;	//遇到结束点  rollback 置为1
			}else{	
				rollback++;	//非结束点 回退数加1
			}
			
			position++;
		}
		
		return words.toString();
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
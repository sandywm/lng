package com.lng.tools;

import java.util.List;

import com.vdurmont.emoji.EmojiParser;


/**
 * 检测app表情符号工具类
 * @author wm
 * @Version : 1.0
 * @ModifiedBy : 修改人
 * @date  2020年1月4日 下午12:02:08
 */
public class EmojiDealUtil  extends EmojiParser{

	/**
	 * @description 获取非表情字符串
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月4日 下午12:34:51
	 * @param input
	 * @return
	 */
	public static String getNonEmojiString1(String input) {  
        int prev = 0;  
        StringBuilder sb = new StringBuilder();  
        List<UnicodeCandidate> replacements = getUnicodeCandidates(input);  
        for (UnicodeCandidate candidate : replacements) {  
          sb.append(input.substring(prev, candidate.getEmojiStartIndex()));  
          prev = candidate.getFitzpatrickEndIndex();  
        }  
        return sb.append(input.substring(prev)).toString();  
	}  

	/**
	 * @description 获取表情字符串
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月4日 下午12:35:07
	 * @param input
	 * @return
	 */
	public static String getEmojiUnicodeString(String input){  
        EmojiTransformer  transformer = new EmojiTransformer() {  
          public String transform(UnicodeCandidate unicodeCandidate) {  
               return unicodeCandidate.getEmoji().getHtmlHexadecimal();  
          }  
        };  
        StringBuilder sb = new StringBuilder();  
        List<UnicodeCandidate> replacements = getUnicodeCandidates(input);  
        for (UnicodeCandidate candidate : replacements) {  
          sb.append(transformer.transform(candidate));  
        }  
        return  parseToUnicode(sb.toString());  
	}

	/**
	 * @description 将字符串转换为html数据存入数据库,同时加入敏感词替换
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月4日 下午12:38:59
	 * @param emoji 要转换的字符串，可以包含或不包括表情包
	 * @return
	 */
	public static String changeEmojiToHtml(String emoji){
	  if(!EmojiDealUtil.getEmojiUnicodeString(emoji).trim().equals("")){
	       String hexadecimal = EmojiParser.parseToHtmlHexadecimal(emoji);
	       return Review.textReview(hexadecimal);
	  }else {
	      return Review.textReview(emoji);
	  }
    }
	
	/**
	 * @description 将表情包还原这样移动端才能识别
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月4日 下午12:39:58
	 * @param string
	 * @return
	 */
	public static String changeStrToEmoji(String string){
		if(!string.equals("")) {
			return EmojiParser.parseToUnicode(string);
		}
        return "";
    }
	
	public static void main(String[] args) {
		System.out.println(EmojiDealUtil.changeEmojiToHtml(""));
		System.out.println(EmojiDealUtil.changeEmojiToHtml("opicContentto腐败麻蛋去你妈的，去你码的picContent腐败\uD83D\uDE0A"));
		System.out.println(EmojiDealUtil.changeStrToEmoji("opicContentto腐败picContent腐败\uD83D\uDE0A"));
	}
}

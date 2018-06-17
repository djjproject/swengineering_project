package kr.or.kpew.kieas.common;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author comkeen
 */
@XmlRootElement
// xml 엘리먼트 사용시 위 지정, xmlrootelement 이노테이션 명시
public class MessageSaver {

    private Map<String, String> words;

    public MessageSaver() {}

    // xml 엘리먼트는 반드시 getter , setter 메소드 정의해야됨
    public Map<String, String> getWords() {
        return this.words;
    }
    
    public void setWords(Map<String, String> words) {
        this.words = words;
    }
}

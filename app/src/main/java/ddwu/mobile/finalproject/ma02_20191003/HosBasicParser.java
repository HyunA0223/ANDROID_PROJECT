package ddwu.mobile.finalproject.ma02_20191003;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class HosBasicParser {

    private static final String TAG = "HosBasicParser";

    private enum TagType { NONE, HOS_NAME, HOS_ADDR, HOS_URL, HOS_NUM, HOS_XPOS, HOS_YPOS, HOS_CODE }

    private final static String ITEM_TAG = "item";
    private final static String NAME_TAG = "yadmNm"; // 병원 이름
    private final static String ADDR_TAG = "addr"; // 병원 주소
    private final static String URL_TAG = "hospUrl"; // 병원 url
    private final static String NUM_TAG = "telno"; // 병원 전화번호
    private final static String XPOS_TAG = "XPos"; // 병원 경도
    private final static String YPOS_TAG = "YPos"; // 병원 위도
    private final static String CODE_TAG = "ykiho"; // 병원 코드(상세 정보 확인 시 필요)

    public HosBasicParser() {
    }

    public ArrayList<HosInfoDto> parse(String xml) {
        ArrayList<HosInfoDto> resultList = new ArrayList();
        HosInfoDto hid = null;
        TagType tagType = TagType.NONE;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml)); // 파싱 대상 지정

            int eventType = parser.getEventType(); // 태그 유형 구분 변수 준비

            while (eventType != XmlPullParser.END_DOCUMENT) { // parsing 수행
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals(ITEM_TAG))
                            hid = new HosInfoDto();
                        else if (tag.equals(NAME_TAG))
                            tagType = TagType.HOS_NAME;
                        else if (tag.equals(ADDR_TAG))
                            tagType = TagType.HOS_ADDR;
                        else if (tag.equals(URL_TAG))
                            tagType = TagType.HOS_URL;
                        else if (tag.equals(NUM_TAG))
                            tagType = TagType.HOS_NUM;
                        else if (tag.equals(XPOS_TAG))
                            tagType = TagType.HOS_XPOS;
                        else if (tag.equals(YPOS_TAG))
                            tagType = TagType.HOS_YPOS;
                        else if (tag.equals(CODE_TAG))
                            tagType = TagType.HOS_CODE;
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ITEM_TAG)) {
                            resultList.add(hid);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case HOS_NAME:
                                hid.setName(parser.getText());
                                break;
                            case HOS_ADDR:
                                hid.setAddress(parser.getText());
                                break;
                            case HOS_URL:
                                hid.setUrl(parser.getText());
                                break;
                            case HOS_NUM:
                                hid.setPhone(parser.getText());
                                break;
                            case HOS_XPOS:
                                hid.setXPos(parser.getText());
                                break;
                            case HOS_YPOS:
                                hid.setYPos(parser.getText());
                                break;
                            case HOS_CODE:
                                hid.setCode(parser.getText());
                                break;

                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

}

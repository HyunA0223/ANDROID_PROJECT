package ddwu.mobile.finalproject.ma02_20191003;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class PhaInfoParser {

    private static final String TAG = "PhaInfoParser";

    private enum TagType { NONE, PHA_NAME, PHA_ADDR, PHA_CITY, PHA_NUM, PHA_XPOS, PHA_YPOS, PHA_CODE }

    private final static String ITEM_TAG = "item";
    private final static String NAME_TAG = "yadmNm"; // 약국 이름
    private final static String ADDR_TAG = "addr"; // 약국 주소
    private final static String CITY_TAG = "sgguCdNm"; // 읍면동
    private final static String NUM_TAG = "telno"; // 약국 전화번호
    private final static String XPOS_TAG = "XPos"; // 약국 위도
    private final static String YPOS_TAG = "YPos"; // 약국 경도
    private final static String CODE_TAG = "ykiho"; // 약국 코드(상세 정보 확인 시 필요)

    public PhaInfoParser() {
    }

    public ArrayList<PhaInfoDto> parse(String xml) {
        ArrayList<PhaInfoDto> resultList = new ArrayList();
        PhaInfoDto hid = null;
        PhaInfoParser.TagType tagType = PhaInfoParser.TagType.NONE;
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
                            hid = new PhaInfoDto();
                        else if (tag.equals(NAME_TAG))
                            tagType = PhaInfoParser.TagType.PHA_NAME;
                        else if (tag.equals(ADDR_TAG))
                            tagType = PhaInfoParser.TagType.PHA_ADDR;
                        else if (tag.equals(CITY_TAG))
                            tagType = PhaInfoParser.TagType.PHA_CITY;
                        else if (tag.equals(NUM_TAG))
                            tagType = PhaInfoParser.TagType.PHA_NUM;
                        else if (tag.equals(XPOS_TAG))
                            tagType = PhaInfoParser.TagType.PHA_XPOS;
                        else if (tag.equals(YPOS_TAG))
                            tagType = PhaInfoParser.TagType.PHA_YPOS;
                        else if (tag.equals(CODE_TAG))
                            tagType = PhaInfoParser.TagType.PHA_CODE;
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ITEM_TAG)) {
                            resultList.add(hid);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case PHA_NAME:
                                hid.setName(parser.getText());
                                break;
                            case PHA_ADDR:
                                hid.setAddress(parser.getText());
                                break;
                            case PHA_CITY:
                                hid.setCity(parser.getText());
                                break;
                            case PHA_NUM:
                                hid.setPhone(parser.getText());
                                break;
                            case PHA_XPOS:
                                hid.setXpos(parser.getText());
                                break;
                            case PHA_YPOS:
                                hid.setYpos(parser.getText());
                                break;
                            case PHA_CODE:
                                hid.setCode(parser.getText());
                                break;

                        }
                        tagType = PhaInfoParser.TagType.NONE;
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

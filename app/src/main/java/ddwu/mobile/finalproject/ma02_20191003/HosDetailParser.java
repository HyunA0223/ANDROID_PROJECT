package ddwu.mobile.finalproject.ma02_20191003;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

public class HosDetailParser {
    private static final String TAG = "HosDetailParser";

    private enum TagType { NONE, NOTRHOL, NOTRSUN, RCVSAT, RCVWEEK }

    private final static String ITEM_TAG = "item";
    private final static String NOTRHOL_TAG = "noTrmtHoli"; // 공휴일 휴진 여부
    private final static String NOTRSUN_TAG = "noTrmtSun"; // 일요일 휴진 여부
    private final static String RCVSAT_TAG = "rcvSat"; // 토요일 운영 시간
    private final static String RCVWEEK_TAG = "rcvWeek"; // 주중 운영 시간

    private boolean hasNoDetailInfo = true;
    
    public HosDetailParser() {
    }

    public HosInfoDto parse(String xml) {
        HosInfoDto hid = null;
        HosInfoDto noHid = new HosInfoDto();
        HosDetailParser.TagType tagType = HosDetailParser.TagType.NONE;
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
                        if (tag.equals(ITEM_TAG)) {
                            hid = new HosInfoDto();
                            hid.setRcvWeek("정보 없음");
                            hid.setRcvSat("정보 없음");
                            hid.setNoTrSun("정보 없음");
                            hid.setNoTrHol("정보 없음");
                            hasNoDetailInfo = false;
                        }
                        else if (tag.equals(NOTRHOL_TAG))
                            tagType = HosDetailParser.TagType.NOTRHOL;
                        else if (tag.equals(NOTRSUN_TAG))
                            tagType = HosDetailParser.TagType.NOTRSUN;
                        else if (tag.equals(RCVSAT_TAG))
                            tagType = HosDetailParser.TagType.RCVSAT;
                        else if (tag.equals(RCVWEEK_TAG))
                            tagType = HosDetailParser.TagType.RCVWEEK;
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ITEM_TAG)) {
                            return hid;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case NOTRHOL:
                                hid.setNoTrHol(parser.getText());
                                break;
                            case NOTRSUN:
                                hid.setNoTrSun(parser.getText());
                                break;
                            case RCVSAT:
                                hid.setRcvSat(parser.getText());
                                break;
                            case RCVWEEK:
                                hid.setRcvWeek(parser.getText());
                                break;
                        }
                        tagType = HosDetailParser.TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
            if (hasNoDetailInfo) {
                noHid.setRcvWeek("정보 없음");
                noHid.setRcvSat("정보 없음");
                noHid.setNoTrSun("정보 없음");
                noHid.setNoTrHol("정보 없음");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return noHid;
    }

}

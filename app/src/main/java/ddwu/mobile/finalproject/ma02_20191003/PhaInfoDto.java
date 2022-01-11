package ddwu.mobile.finalproject.ma02_20191003;

import java.io.Serializable;

public class PhaInfoDto implements Serializable {
    private long _id;
    private String name;    // 약국 이름
    private String address; // 주소
    private String city;    // 읍면동
    private String phone;   // 약국 전화번호
    private String xpos;    // 약국 경도
    private String ypos;    // 약국 위도
    private String code;

    public long get_id() { return _id; }
    public void set_id(long _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPhone() { return phone; }
    public void setPhone(String num) { this.phone = num; }

    public String getXpos() {   return xpos; }
    public void setXpos(String xpos) {   this.xpos = xpos;  }

    public String getYpos() {   return ypos;  }
    public void setYpos(String ypos) {  this.ypos = ypos; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

}

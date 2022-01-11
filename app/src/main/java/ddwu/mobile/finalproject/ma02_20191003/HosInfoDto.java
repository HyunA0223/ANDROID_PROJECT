package ddwu.mobile.finalproject.ma02_20191003;

import java.io.Serializable;

public class HosInfoDto implements Serializable {
    private long _id;
    // basic
    private String name;
    private String address;
    private String phone;
    private String url;
    private String code;

    // detail
    private String noTrHol;
    private String noTrSun;
    private String rcvSat;
    private String rcvWeek;
    private String xPos;
    private String yPos;

    // subject
    private String subject;

    public long get_id() { return _id; }
    public void set_id(long _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String num) { this.phone = num; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNoTrHol() {  return noTrHol;   }
    public void setNoTrHol(String noTrHol) {  this.noTrHol = noTrHol;   }

    public String getNoTrSun() {   return noTrSun;    }
    public void setNoTrSun(String noTrSun) {   this.noTrSun = noTrSun;   }

    public String getRcvSat() {   return rcvSat;  }
    public void setRcvSat(String rcvSat) {  this.rcvSat = rcvSat; }

    public String getRcvWeek() {  return rcvWeek;  }
    public void setRcvWeek(String rcvWeek) {  this.rcvWeek = rcvWeek;  }

    public String getSubject() {return subject;}
    public void setSubject(String subject) {this.subject = subject;}

    public String getXPos() {   return xPos;  }
    public void setXPos(String xPos) {    this.xPos = xPos;  }

    public String getYPos() {   return yPos;  }
    public void setYPos(String yPos) {   this.yPos = yPos;  }
}

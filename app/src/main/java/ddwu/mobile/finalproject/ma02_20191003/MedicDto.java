package ddwu.mobile.finalproject.ma02_20191003;

import java.io.Serializable;

public class MedicDto implements Serializable {
    
    private long _id;
    private String name;
    private String date;
    private String time;
    private String image;
    private String memo;

    public String getName() {  return name;  }
    public void setName(String name) {  this.name = name;  }

    public long get_id() {  return _id;  }
    public void set_id(long _id) {  this._id = _id;  }

    public String getDate() {   return date;  }
    public void setDate(String date) {   this.date = date;  }

    public String getTime() {   return time;  }
    public void setTime(String time) {   this.time = time;  }

    public String getImage() {   return image; }
    public void setImage(String image) {   this.image = image;  }

    public String getMemo() {   return memo;    }
    public void setMemo(String memo) {  this.memo = memo;   }
}

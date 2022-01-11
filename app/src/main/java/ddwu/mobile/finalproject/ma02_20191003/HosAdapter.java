package ddwu.mobile.finalproject.ma02_20191003;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HosAdapter extends BaseAdapter {
    public static final String TAG = "BasicAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<HosInfoDto> list;
    private NetworkManager networkManager = null;
    //private ImageFileManager imageFileManager = null;


    public HosAdapter(Context context, int resource, ArrayList<HosInfoDto> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        //imageFileManager = new ImageFileManager(context);
        networkManager = new NetworkManager(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HosInfoDto getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView with position : " + position);
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvHosName = view.findViewById(R.id.name);
            viewHolder.tvAddress = view.findViewById(R.id.address);
            viewHolder.tvPhone = view.findViewById(R.id.phone);
            viewHolder.tvXpos = view.findViewById(R.id.xPos);
            viewHolder.tvYpos = view.findViewById(R.id.yPos);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        HosInfoDto dto = list.get(position);

        viewHolder.tvHosName.setText(dto.getName());
        viewHolder.tvAddress.setText(dto.getAddress());
        viewHolder.tvPhone.setText(dto.getPhone());
        viewHolder.tvXpos.setText(dto.getXPos());
        viewHolder.tvYpos.setText(dto.getYPos());

        /*작성할 부분*/
//         dto의 이미지 주소에 해당하는 이미지 파일이 내부저장소에 있는지 확인
//         ImageFileManager 의 내부저장소에서 이미지 파일 읽어오기 사용
//         이미지 파일이 있을 경우 bitmap, 없을 경우 null 을 반환하므로 bitmap 이 있으면 이미지뷰에 지정
//         없을 경우 GetImageAsyncTask 를 사용하여 이미지 파일 다운로드 수행


        // 파일에 있는지 확인
        // dto 의 이미지 주소 정보로 이미지 파일 읽기


        return view;
    }


    public void setList(ArrayList<HosInfoDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvHosName = null;
        public TextView tvAddress = null;
        public TextView tvPhone = null;
        public TextView tvXpos = null;
        public TextView tvYpos = null;
    }
}
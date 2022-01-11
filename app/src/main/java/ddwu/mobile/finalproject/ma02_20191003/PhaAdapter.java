package ddwu.mobile.finalproject.ma02_20191003;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PhaAdapter extends BaseAdapter {
    public static final String TAG = "PhaAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<PhaInfoDto> list;
    private NetworkManager networkManager = null;
    //private ImageFileManager imageFileManager = null;

    public PhaAdapter(Context context, int resource, ArrayList<PhaInfoDto> list) {
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
    public PhaInfoDto getItem(int position) {
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
        PhaAdapter.ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new PhaAdapter.ViewHolder();
            viewHolder.tvPhaName = view.findViewById(R.id.name);
            viewHolder.tvAddress = view.findViewById(R.id.address);
            viewHolder.tvPhone = view.findViewById(R.id.phone);
            viewHolder.tvXpos = view.findViewById(R.id.xPos);
            viewHolder.tvYpos = view.findViewById(R.id.yPos);
            view.setTag(viewHolder);
        } else {
            viewHolder = (PhaAdapter.ViewHolder) view.getTag();
        }

        PhaInfoDto dto = list.get(position);
        viewHolder.tvPhaName.setText(dto.getName());
        viewHolder.tvAddress.setText(dto.getAddress());
        viewHolder.tvPhone.setText(dto.getPhone());
        viewHolder.tvXpos.setText(dto.getXpos());
        viewHolder.tvYpos.setText(dto.getYpos());

        return view;
    }


    public void setList(ArrayList<PhaInfoDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView tvPhaName = null;
        public TextView tvAddress = null;
        public TextView tvPhone = null;
        public TextView tvXpos = null;
        public TextView tvYpos = null;
    }
}
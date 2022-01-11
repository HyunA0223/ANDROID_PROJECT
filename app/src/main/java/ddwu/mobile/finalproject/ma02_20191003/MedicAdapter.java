package ddwu.mobile.finalproject.ma02_20191003;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MedicAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<MedicDto> list;

    public MedicAdapter(Context context, int layout, ArrayList<MedicDto> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {   return list.size();  }

    @Override
    public Object getItem(int position) {  return list.get(position); }

    @Override
    public long getItemId(int position) {    return list.get(position).get_id();  }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MedicAdapter.ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new MedicAdapter.ViewHolder();
            viewHolder.lvMedicName = view.findViewById(R.id.lvMedicName);
            viewHolder.lvTime = view.findViewById(R.id.lvTime);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MedicAdapter.ViewHolder) view.getTag();
        }

        MedicDto dto = list.get(position);
        viewHolder.lvMedicName.setText(dto.getName());
        viewHolder.lvTime.setText(dto.getTime());

        return view;
    }

    public void setList(ArrayList<MedicDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //    ※ findViewById() 호출 감소를 위해 필수로 사용할 것
    static class ViewHolder {
        public TextView lvMedicName = null;
        public TextView lvTime = null;
        public TextView lvRepit = null;
    }
}

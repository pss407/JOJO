package com.example.jojo.bangguseok.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jojo.bangguseok.R;

import java.util.ArrayList;

/**
 * Created by pc-20 on 2017-12-13.
 */

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<com.example.jojo.bangguseok.chatting.ChatVO> chatData;
    private LayoutInflater inflater;
    private String id;


    public ChatAdapter(Context applicationContext, int talklist, ArrayList<ChatVO> list, String id) {
        this.context = applicationContext;
        this.layout = talklist;
        this.chatData = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.id= id;
    }

    @Override
    public int getCount() { // 전체 데이터 개수
        return chatData.size();
    }

    @Override
    public Object getItem(int position) { // position번째 아이템
        return chatData.get(position);
    }

    @Override
    public long getItemId(int position) { // position번째 항목의 id인데 보통 position
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //항목의 index, 전에 inflate 되어있는 view, listView

//첫항목을 그릴때만 inflate 함 다음거부터는 매개변수로 넘겨줌 (느리기때문) : recycle이라고 함
        ViewHolder holder;

        if(convertView == null){
//어떤 레이아웃을 만들어 줄 것인지, 속할 컨테이너, 자식뷰가 될 것인지
            convertView = inflater.inflate(layout, parent, false); //아이디를 가지고 view를 만든다
            holder = new ViewHolder();
            holder.img= (ImageView)convertView.findViewById(R.id.iv_profile);
            holder.tv_msg = (TextView)convertView.findViewById(R.id.tv_content);
            holder.tv_name = (TextView)convertView.findViewById(R.id.tv_id);
            holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tv_time.setVisibility(View.VISIBLE);
        holder.tv_name.setVisibility(View.VISIBLE);
        holder.tv_msg.setVisibility(View.VISIBLE);
        holder.img.setVisibility(View.VISIBLE);

        holder.img.setImageResource(chatData.get(position).getImageID()); // 해당 사람의 프사 가져옴
        holder.tv_msg.setText(chatData.get(position).getContent());
        holder.tv_time.setText(chatData.get(position).getTime());
        holder.tv_name.setText(chatData.get(position).getId());

        return convertView;
    }

    //뷰홀더패턴
    public class ViewHolder{
        ImageView img;
        TextView tv_msg;
        TextView tv_time;
        TextView tv_name;
    }

}

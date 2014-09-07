/*
 * Copyright (c) 2013. 1010.am
 *
 * You may obtain a copy of the License at
 *
 *      http://1010.am
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.dingding.snail.forepaly.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cc.dingding.snail.forepaly.app.R;
import cc.dingding.snail.forepaly.app.models.FooterDataModel;
import cc.dingding.snail.forepaly.app.utils.DeviceUtils;


/**
 * Common footer fragment with the same pattern.
 * 
 * Created by sunkai on 13-9-1.
 * <p>
 * modified by snail on 13-10-17.
 * 
 * <p>
 * 去掉构造函数 使用bundle获取参数，解决空指针异常问题
 * <p>
 * modified by <b>孙浩</b> on 14-2-16.
 */
public class FooterFragment extends Fragment {

    private List<FooterDataModel> itemPair;

    // public FooterFragment(List<FooterData> list) {
    // this.itemPair = list;
    // }

    public FooterFragment() {
    }

    FooterClickListner footerClickListner;

    /*
     * footer click
     */
    public interface FooterClickListner {
        public void onFooterClick(int position);
    }

    List<RelativeLayout> items = new ArrayList<RelativeLayout>();

    List<ImageView> mIsNewList = new ArrayList<ImageView>();
    List<RadioButton> mRbList = new ArrayList<RadioButton>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            footerClickListner = (FooterClickListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    public void changeDisplayItem(int v) {
        for (int i = 0; i < mRbList.size(); ++i) {
            if (i == v - 1) {

                mRbList.get(i).setCompoundDrawablesWithIntrinsicBounds(0, this.itemPair.get(i).getIdSelected(), 0, 0);
                // mRbList.get(i).setBackgroundResource(this.itemPair.get(i).getIdSelected());
                mRbList.get(i).setChecked(true);
            } else {
                mRbList.get(i).setCompoundDrawablesWithIntrinsicBounds(0, this.itemPair.get(i).getId(), 0, 0);

                // mRbList.get(i).setBackgroundResource(this.itemPair.get(i).getId());
                mRbList.get(i).setChecked(false);
            }
        }
    }

    public void recoveryDisplayItem(int v) {
        for (int i = 0; i < mRbList.size(); ++i) {
            if (i == v - 1) {
                mRbList.get(i).setChecked(false);
            }
        }
    }

    /**
     * change have new messages
     * 
     * @param flag
     * @param i
     */
    public void changeStatus(Boolean flag, int i) {
        if (i < mIsNewList.size() && i >= 0) {
            int views = flag ? View.VISIBLE : View.INVISIBLE;
            mIsNewList.get(i).setVisibility(views);
        }
    }

    private int getFooterItemWidth() {
        int size = this.itemPair.size();
        return (DeviceUtils.deviceWidth(this.getActivity()) - (size - 1) * DeviceUtils.dip2px(this.getActivity(), 2)) / size;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        itemPair = (List<FooterDataModel>) bundle.getSerializable("footdata");
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.footer_container, container, false);
        LinearLayout viewParent = (LinearLayout) view.findViewById(R.id.footer_parent);
        for (int i = 1; i <= this.itemPair.size(); ++i) {
            final int index = i;
            FooterDataModel temp = this.itemPair.get(i - 1);
            RelativeLayout footerItem = (RelativeLayout) inflater.inflate(R.layout.footer_item, container, false);
            ViewGroup.LayoutParams params = footerItem.getLayoutParams();
            params.width = this.getFooterItemWidth();
            footerItem.setLayoutParams(params);
            RadioButton rbButton = (RadioButton) footerItem.findViewById(R.id.tab);
            rbButton.setCompoundDrawablesWithIntrinsicBounds(0, temp.getId(), 0, 0);
            rbButton.setText(temp.getText());

            ImageView isNew = (ImageView) footerItem.findViewById(R.id.footer_message_isnew);
            int views = (temp.getIsNew()) ? View.VISIBLE : View.INVISIBLE;
            isNew.setVisibility(views);

            items.add(footerItem);
            mIsNewList.add(isNew);
            mRbList.add(rbButton);
            viewParent.addView(footerItem);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    footerClickListner.onFooterClick(index);
                }
            };
            rbButton.setOnClickListener(listener);
            footerItem.setOnClickListener(listener);
        }
        changeDisplayItem(1);
        return view;
    }
}

package com.atop.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ssm_test.R;

public class NodeListAdapter extends BaseExpandableListAdapter {
	
    private static final String TAG = "[Chord][NodeList]";
    private static final String TAGClass = "NodeListAdapter : ";
    
    private LayoutInflater mInflater = null;
    private Context mContext = null;
    private NodeViewHolder mNodeViewHolder = null;
    private ProgressViewHolder mProgressViewHolder = null;
    private List<NodeInfo> mNodeInfoList = null;
    
    private boolean bCheckMode = true;
    private boolean mIsSecureChannelFrag = false;
    private IFileCancelListener mFileCancelListener = null;
    
    private HashMap<String, Integer> fileCntHashMap = new HashMap<String, Integer>();
    private HashMap<String, Boolean> fileCancelHashMap = new HashMap<String, Boolean>();

    public interface IFileCancelListener {
        void onFileCanceled(String channel, String node, String trId, int index, boolean bMulti);
    }

    public NodeListAdapter(Context context, IFileCancelListener fileCancelListener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mNodeInfoList = new CopyOnWriteArrayList<NodeInfo>();
        mFileCancelListener = fileCancelListener;
    }

    /* Information about the joined node */
    class NodeInfo {

        String interfaceName = null;

        String nodeName = null;

        int nodeNumber = 0;

        boolean bChecked = false;

        ArrayList<ProgressInfo> progressInfoList = new ArrayList<ProgressInfo>();

        NodeInfo(String interfaceName, String nodeName, int nodeNumber, boolean bChecked) {
            this.interfaceName = interfaceName;
            this.nodeName = nodeName;
            this.bChecked = bChecked;
            this.nodeNumber = nodeNumber;
        }
    }

    /* Information about the progressBar of the file transfer */
    class ProgressInfo {
        boolean bMulti = false;

        boolean bSend = false;

        String trId = null;

        int fileIndex = 0;

        int progress = 0;
    }

    class ProgressViewHolder {
        ImageView loadingicon_imageView;

        Button fileCancel_btn;

        ProgressBar progressbar;

        TextView fileCnt_textView;
    }

    class NodeViewHolder {
        TextView mNodename_textView;

        CheckBox mbSend_checkbox;
    }

    public void addNode(String interfaceName, String nodeName, int nodeNumber) {
        NodeInfo nodeInfo = new NodeInfo(interfaceName, nodeName, nodeNumber, false);
        mNodeInfoList.add(nodeInfo);

        notifyDataSetChanged();
    }

    public void removeNode(String interfaceName, String nodeName) {
        int position = getNodePosition(interfaceName, nodeName);
        if (position == -1) {
            Log.d(TAG, TAGClass + "removeNode() : no such a node - " + nodeName);
            return;
        }
        mNodeInfoList.remove(position);

        notifyDataSetChanged();
    }

    public void removeNodeGroup(String interfaceName) {

        List<NodeInfo> nodeList = mNodeInfoList;
        for (NodeInfo nodeInfo : nodeList) {
            if (nodeInfo.interfaceName.equals(interfaceName)) {
                Log.d(TAG, TAGClass + "removeNodeGroup(" + interfaceName + ")");
                removeNode(interfaceName, nodeInfo.nodeName);
            }
        }

        notifyDataSetChanged();
    }

    public void removeNodeAll() {
        mNodeInfoList.clear();
        notifyDataSetChanged();
    }

    /* Get the list of nodes that you checked. */
    public ArrayList<String[][]> getCheckedNodeList() {
        ArrayList<String[][]> checkedList = new ArrayList<String[][]>();
        String checkedNodeInfo[][] = new String[mNodeInfoList.size()][2];
        int i = 0;

        for (NodeInfo nodeInfo : mNodeInfoList) {
            if (nodeInfo.bChecked) {
                checkedNodeInfo[i][0] = nodeInfo.interfaceName;
                checkedNodeInfo[i][1] = nodeInfo.nodeName;
                checkedList.add(checkedNodeInfo);
                i++;
            }
        }

        return checkedList;
    }

    public ArrayList<String> getNodeList() {
        ArrayList<String> nodeNameList = new ArrayList<String>();
        for (NodeInfo nodeInfo : mNodeInfoList) {
            nodeNameList.add(nodeInfo.nodeName);
        }

        return nodeNameList;
    }

    public void setCheckMode(boolean checkMode) {
        bCheckMode = checkMode;
    }

    public void setSecureChannelFrag(boolean isSecureChannelFrag) {
        mIsSecureChannelFrag = isSecureChannelFrag;
    }

    /* Set the total count of files. */
    public void setFileTotalCnt(String interfaceName, String node, int fileTotalCnt, String trId) {

        int nodePosition = getNodePosition(interfaceName, node);
        if (nodePosition == -1) {
            Log.d(TAG, TAGClass + "setFileTotalCnt() : no such a node - " + node);
            return;
        }

        fileCntHashMap.put(trId, fileTotalCnt);

    }

    /* Add or Update the progressBar */
    public void setProgressUpdate(String interfaceName, String node, String trId, int fileIndex,
            int progress, boolean bMulti, boolean bSend) {

        int nodePosition = getNodePosition(interfaceName, node);
        if (nodePosition == -1) {
            Log.d(TAG, TAGClass + "setProgressUpdate(fileIdx[" + fileIndex
                    + "]) : no such a node - " + node);
            return;
        }

        int progressPosition = getProgressPosition(nodePosition, fileIndex, trId);

        if (progressPosition == -1) { // Add the progressBar
            if (null != fileCancelHashMap.get(trId)) {
                Log.d(TAG, TAGClass + "fileChunk is received/sent after click the cancel button.");
                fileCancelHashMap.remove(trId);
                return;
            }

            ProgressInfo progressInfo = new ProgressInfo();
            progressInfo.trId = trId;
            progressInfo.bMulti = bMulti;
            progressInfo.fileIndex = fileIndex;
            progressInfo.progress = progress;
            progressInfo.bSend = bSend;

            addProgress(nodePosition, progressInfo);

        } else { // Update the progressBar - save the percentage of the file
                 // transfer
            ArrayList<ProgressInfo> progressList = mNodeInfoList.get(nodePosition).progressInfoList;
            progressList.get(progressPosition).progress = progress;

            notifyDataSetChanged();
        }
    }

    /* Add the progressBar */
    public void addProgress(int nodePosition, ProgressInfo progressInfo) {
        ArrayList<ProgressInfo> progressInfoList = mNodeInfoList.get(nodePosition).progressInfoList;
        progressInfoList.add(progressInfo);

        notifyDataSetChanged();
    }

    /* Remove the progressBar */
    public void removeProgress(int fileIndex, String trId) {
        for (int i = 0; i < getGroupCount(); i++) {
            NodeInfo node = mNodeInfoList.get(i);
            for (int j = 0; j < getChildrenCount(i); j++) {

                ProgressInfo progressInfo = node.progressInfoList.get(j);
                if (progressInfo.fileIndex == fileIndex && progressInfo.trId.equals(trId)) {
                    mNodeInfoList.get(i).progressInfoList.remove(j);
                }
            }
        }

        notifyDataSetChanged();
    }

    /* Remove the canceled file transfer's progressBar. */
    public void removeCanceledProgress(String interfaceName, String nodeName, String trId) {
        int nodePosition = getNodePosition(interfaceName, nodeName);
        if (nodePosition == -1) {
            Log.d(TAG, TAGClass + "removeCanceledProgress() : no such a node - " + nodeName);
            return;
        }

        // Find the canceled file transfer's trId
        for (Iterator<ProgressInfo> iterator = mNodeInfoList.get(nodePosition).progressInfoList
                .iterator(); iterator.hasNext();) {
            ProgressInfo progressInfo = iterator.next();
            if (progressInfo.trId.equals(trId)) {
                iterator.remove();
            }
        }

        notifyDataSetChanged();
    }

    /* Get the node's position. */
    private int getNodePosition(String interfaceName, String nodeName) {

        for (int i = 0; i < getGroupCount(); i++) {
            if (mNodeInfoList.get(i).interfaceName.equals(interfaceName)
                    && mNodeInfoList.get(i).nodeName.equals(nodeName)) {
                return i;
            }
        }

        return -1;
    }

    /* Get the progressBar's position. */
    private int getProgressPosition(int nodePosition, int fileIndex, String trId) {
        NodeInfo node = mNodeInfoList.get(nodePosition);
        for (int i = 0; i < getChildrenCount(nodePosition); i++) {

            ProgressInfo progressInfo = node.progressInfoList.get(i);
            if (progressInfo.fileIndex == fileIndex && progressInfo.trId.equals(trId)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object getChild(int nodePosition, int progressPosition) {

        return mNodeInfoList.get(nodePosition).progressInfoList.get(progressPosition);
    }

    @Override
    public long getChildId(int nodePosition, int progressPosition) {

        return progressPosition;
    }

    @Override
    public View getChildView(int nodePosition, int progressPosition, boolean isLastProgress,
            View view, ViewGroup parent) {

        View v = view;
        if (null == v) {
            mProgressViewHolder = new ProgressViewHolder();
            v = mInflater.inflate(R.layout.progress_list_item, null);
            mProgressViewHolder.loadingicon_imageView = (ImageView) v
                    .findViewById(R.id.loadingicon_imageView);
            mProgressViewHolder.fileCancel_btn = (Button) v.findViewById(R.id.fileCancel_btn);
            mProgressViewHolder.progressbar = (ProgressBar) v.findViewById(R.id.progressBar);
            mProgressViewHolder.fileCnt_textView = (TextView) v.findViewById(R.id.fileCnt_textView);
            v.setTag(mProgressViewHolder);

        } else {
            mProgressViewHolder = (ProgressViewHolder) v.getTag();
        }

        final NodeInfo nodeInfo = mNodeInfoList.get(nodePosition);
        final ProgressInfo progressInfo = nodeInfo.progressInfoList.get(progressPosition);

        // Set the progressBar's icon to distinguish between sending and
        // receiving the file transfer.
        if (progressInfo.bSend) {
            mProgressViewHolder.loadingicon_imageView
                    .setBackgroundResource(R.drawable.ic_file_sending);

        } else {
            mProgressViewHolder.loadingicon_imageView
                    .setBackgroundResource(R.drawable.ic_file_receiving);
        }

        // Set the the percentage of the file transfer and count of files.
        mProgressViewHolder.progressbar.setProgress(progressInfo.progress);
        mProgressViewHolder.fileCnt_textView.setText("(" + progressInfo.fileIndex + "/"
                + fileCntHashMap.get(progressInfo.trId) + ")");

        // When you click a button of the file transfer ..
        mProgressViewHolder.fileCancel_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mFileCancelListener.onFileCanceled(nodeInfo.interfaceName, nodeInfo.nodeName,
                        progressInfo.trId, progressInfo.fileIndex, progressInfo.bMulti);
                fileCancelHashMap.put(progressInfo.trId, true);
                Log.d(TAG, TAGClass + "onClick() : " + progressInfo.trId);
            }
        });

        return v;

    }

    @Override
    public int getChildrenCount(int nodePosition) {

        return mNodeInfoList.get(nodePosition).progressInfoList.size();
    }

    @Override
    public Object getGroup(int nodePosition) {

        return mNodeInfoList.get(nodePosition);
    }

    @Override
    public int getGroupCount() {

        return mNodeInfoList.size();
    }

    @Override
    public long getGroupId(int nodePosition) {

        return nodePosition;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded, View view, ViewGroup parent) {

        View v = view;
        if (null == v) {
            mNodeViewHolder = new NodeViewHolder();

            v = mInflater.inflate(R.layout.node_list_item, parent, false);
            mNodeViewHolder.mNodename_textView = (TextView) v.findViewById(R.id.nodeName_textview);
            mNodeViewHolder.mbSend_checkbox = (CheckBox) v.findViewById(R.id.bSend_checkbox);

            v.setTag(mNodeViewHolder);

        } else {
            mNodeViewHolder = (NodeViewHolder) v.getTag();
        }

        if (mIsSecureChannelFrag) {
            LinearLayout mNodeListItem = (LinearLayout) v.findViewById(R.id.nodeListItem_layout);
            mNodeListItem.setPadding(20, 15, 15, 15);
        }

        // set a name of the node.
        final NodeInfo nodeInfo = mNodeInfoList.get(position);
        mNodeViewHolder.mNodename_textView.setText("Node" + nodeInfo.nodeNumber + " : "
                + nodeInfo.nodeName + " / "  + " [" + nodeInfo.interfaceName + "]");

        if (bCheckMode) {
            mNodeViewHolder.mbSend_checkbox.setVisibility(View.VISIBLE);

            mNodeViewHolder.mbSend_checkbox
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked && !nodeInfo.bChecked) {
                                nodeInfo.bChecked = true;
                            } else if (!isChecked && nodeInfo.bChecked) {
                                nodeInfo.bChecked = false;
                            }
                        }
                    });
        } else {
            mNodeViewHolder.mbSend_checkbox.setVisibility(View.GONE);
        }

        // set the checkBox
        if (nodeInfo.bChecked) {
            mNodeViewHolder.mbSend_checkbox.setChecked(true);
        } else {
            mNodeViewHolder.mbSend_checkbox.setChecked(false);
        }

        return v;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {

        return true;
    }



}

package com.vijay.jsonwizard.customviews;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.LinearLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.vijay.jsonwizard.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeViewDialog extends Dialog implements TreeNode.TreeNodeClickListener {
    private static final String KEY_NODES = "nodes";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_NAME = "name";

    private final Context context;
    private ArrayList<String> value;
    private TreeNode rootNode;

    public TreeViewDialog(Context context, JSONArray structure, ArrayList<String> defaultValue) throws
            JSONException {
        super(context);
        this.context = context;
        init(structure, defaultValue);
    }

    public TreeViewDialog(Context context, int theme, JSONArray structure, ArrayList<String> defaultValue) throws JSONException {
        super(context, theme);
        this.context = context;
        init(structure, defaultValue);
    }

    protected TreeViewDialog(Context context, boolean cancelable, OnCancelListener
            cancelListener, JSONArray structure, ArrayList<String> defaultValue) throws JSONException {
        super(context, cancelable, cancelListener);
        this.context = context;
        init(structure, defaultValue);
    }

    private void init(JSONArray nodes, ArrayList<String> defaultValue) throws JSONException {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_tree_view);
        LinearLayout canvas = (LinearLayout) this.findViewById(R.id.canvas);


        this.value = new ArrayList<>();

        JSONObject rootObject = new JSONObject();
        rootObject.put(KEY_NAME, "");
        rootObject.put(KEY_LEVEL, "");
        rootObject.put(KEY_NODES, nodes);
        rootNode = constructTreeView(rootObject, null, defaultValue);

        AndroidTreeView androidTreeView = new AndroidTreeView(context, rootNode);
        androidTreeView.setDefaultContainerStyle(R.style.TreeNodeStyle);

        canvas.addView(androidTreeView.getView());
    }

    private TreeNode constructTreeView(JSONObject structure, TreeNode parent, ArrayList<String> defaultValue) throws
            JSONException {
        String name = structure.getString(KEY_NAME);
        TreeNode curNode = new TreeNode(name);
        curNode.setClickListener(this);
        curNode.setViewHolder(new SelectableItemHolder(context, structure.getString(KEY_LEVEL)));
        if (parent == null) {
            curNode.setSelectable(false);
        }
        if (structure.has(KEY_NODES)) {
            JSONArray options = structure.getJSONArray(KEY_NODES);
            for (int i = 0; i < options.length(); i++) {
                constructTreeView(options.getJSONObject(i), curNode, defaultValue);
            }
        }

        if (parent != null) {
            if(parent.getLevel() == 0) {
                setSelectedValue(curNode, 0, defaultValue);
            }
            parent.addChild(curNode);
        }

        return curNode;
    }

    @Override
    public void onClick(TreeNode node, Object value) {
        value = new ArrayList<>();
        if (node.getChildren().size() == 0) {
            ArrayList<String> reversedValue = new ArrayList<>();
            retrieveValue(node, reversedValue);

            Collections.reverse(reversedValue);
            this.value = reversedValue;

            dismiss();
        }
    }

    private static void retrieveValue(TreeNode node, ArrayList<String> value) {
        if (node.getParent() != null) {
            value.add((String) node.getValue());
            retrieveValue(node.getParent(), value);
        }
    }

    public ArrayList<String> getValue() {
        return this.value;
    }

    public void setValue(final ArrayList<String> value) {
        this.value = value;
    }

    private static void setSelectedValue(TreeNode treeNode, int level, ArrayList<String> defaultValue) {
        if (treeNode != null) {
            if (defaultValue != null) {
                if (level >= 0 && level < defaultValue.size()) {
                    String levelValue = defaultValue.get(level);
                    String nodeValue = (String) treeNode.getValue();
                    if (nodeValue != null && nodeValue.equals(levelValue)) {
                        treeNode.setExpanded(true);
                        List<TreeNode> children = treeNode.getChildren();
                        for (TreeNode curChild : children) {
                            setSelectedValue(curChild, level + 1, defaultValue);
                        }
                        return;
                    }
                }
            }

            treeNode.setExpanded(false);
        }
    }
}

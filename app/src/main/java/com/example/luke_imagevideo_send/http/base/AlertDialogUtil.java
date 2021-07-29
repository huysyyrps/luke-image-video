package com.example.luke_imagevideo_send.http.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.cehouyi.bean.DictUnit;

import java.util.List;


public class AlertDialogUtil {
    private Context context;
    Dialog dialog;

    public AlertDialogUtil(Context context) {
        this.context = context;
    }

    public void showDialog(String description, final AlertDialogCallBack alertDialogCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_title, null, false);
            TextView tv_content = (TextView) view.findViewById(R.id.content);
            TextView tv_yes = (TextView) view.findViewById(R.id.yes);
            TextView tv_no = (TextView) view.findViewById(R.id.no);
            tv_content.setText(description);
            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogCallBack.cancel();
                }
            });

            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogCallBack.confirm("");
                }
            });
            dialog.getWindow().setContentView(view);
        }
    }

    public void showLoginDialog(String description, final AlertDialogLoginCallBack alertDialogLoginCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_title, null, false);
            TextView tv_content = (TextView) view.findViewById(R.id.content);
            TextView tv_yes = (TextView) view.findViewById(R.id.yes);
            TextView tv_no = (TextView) view.findViewById(R.id.no);
            tv_content.setText(description);
            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogLoginCallBack.cancel();
                }
            });

            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogLoginCallBack.confirm();
                }
            });
            dialog.getWindow().setContentView(view);
        }
    }

    public void showSmallDialog(String description, final DialogCallBack alertDialogCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_title, null, false);
            TextView tv_content = (TextView) view.findViewById(R.id.content);
            TextView tv_yes = (TextView) view.findViewById(R.id.yes);
            TextView tv_no = (TextView) view.findViewById(R.id.no);
            tv_content.setText(description);
            tv_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogCallBack.cancel();
                }
            });

            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogCallBack.confirm("",dialog);
                }
            });
            dialog.getWindow().setContentView(view);
        }
    }

    public void showListDialog(String description, String change, List<DictUnit> list3, final MenuAlertDialogCallBack alertDialogCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_list, null, false);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            BaseRecyclerAdapter mAdapter = new BaseRecyclerAdapter<DictUnit>(context, R.layout.down, list3) {
                @Override
                public void convert(BaseViewHolder holder, DictUnit dictUnit) {
                    if (change.equals("change")){
                        holder.setGoneText(R.id.contentTextView);
                        holder.setVisLinearLayout(R.id.llEditText);
                    }else {
                        holder.setVisText(R.id.contentTextView);
                        holder.setGoneLinearLayout(R.id.llEditText);
                    }
                    holder.setText(R.id.contentTextView, dictUnit.name);
                    holder.setOnClickListener(R.id.contentTextView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            alertDialogCallBack.confirm(dictUnit.field,dictUnit.name);
                        }
                    });
                    holder.setOnClickListener(R.id.tvSure, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            EditText editText = holder.getView(R.id.contentEditText);
                            alertDialogCallBack.confirm(dictUnit.field,editText.getText().toString()+"mm");
                        }
                    });
                }
            };
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            dialog.getWindow().setContentView(view);
        }
    }

    public void showListStringDialog(String description, List<String> list3, final MenuAlertDialogCallBack alertDialogCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_list, null, false);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            BaseRecyclerAdapter mAdapter = new BaseRecyclerAdapter<String>(context, R.layout.dialog_setting_item, list3) {
                @Override
                public void convert(BaseViewHolder holder, String string) {
                    holder.setText(R.id.tvData, string);
                    holder.setOnClickListener(R.id.tvData, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            alertDialogCallBack.confirm(string,string);
                        }
                    });
                }
            };
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            dialog.getWindow().setContentView(view);
        }
    }

    public void showSmallDialog(String description) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_one_title, null, false);
            TextView tv_content = (TextView) view.findViewById(R.id.content);
            TextView tv_yes = (TextView) view.findViewById(R.id.yes);
            tv_content.setText(description);
            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setContentView(view);
        }
    }

    public void showSmallDialogCli(String description, final AlertDialogCallBack alertDialogCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_one_title, null, false);
            TextView tv_content = (TextView) view.findViewById(R.id.content);
            TextView tv_yes = (TextView) view.findViewById(R.id.yes);
            tv_content.setText(description);
            tv_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogCallBack.confirm("");
                }
            });
            dialog.getWindow().setContentView(view);
        }
    }

    public void showImageDialog(final AlertDialogCallBack alertDialogCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new Dialog(context);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_image, null, false);
            EditText editText = (EditText) view.findViewById(R.id.edittext);
            TextView tvYes = (TextView) view.findViewById(R.id.yes);
            TextView tvNo = (TextView) view.findViewById(R.id.no);
            TextView tvSave = (TextView) view.findViewById(R.id.save);
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogCallBack.confirm(editText.getText().toString());
                }
            });

            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogCallBack.cancel();
                }
            });

            tvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    alertDialogCallBack.save(editText.getText().toString().trim());
                }
            });
            dialog.getWindow().setContentView(view);
        }
    }

    public void showImageNameSelect(final DialogCallBackTwo alertDialogCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new Dialog(context);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_imagename, null, false);
            EditText editText = (EditText) view.findViewById(R.id.edittext);
            TextView tvYes = (TextView) view.findViewById(R.id.yes);
            TextView tvCover = (TextView) view.findViewById(R.id.cover);
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogCallBack.confirm(editText.getText().toString(),dialog,editText);
                }
            });

            tvCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogCallBack.cancel(editText.getText().toString(),dialog);
                }
            });

            dialog.getWindow().setContentView(view);
        }
    }

    public void showWifiSetting(Context context,String ssid,String pwd,final DialogCallBack dialogCallBack) {
        if (dialog == null || !dialog.isShowing()) {
            dialog = new Dialog(context);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_with_wifisetting, null, false);
            TextView tvSSID = (TextView) view.findViewById(R.id.tvSSID);
            TextView tvPWD = (TextView) view.findViewById(R.id.tvPWD);
            Button btnCapySSID = (Button) view.findViewById(R.id.btnCapySSID);
            Button btnCopyPwd = (Button) view.findViewById(R.id.btnCapyPWD);
            TextView tvSetting = (TextView) view.findViewById(R.id.tvSetting);
            TextView tvCancle = (TextView) view.findViewById(R.id.tvCancle);

            tvSSID.setText(ssid);
            tvPWD.setText(pwd);

            btnCapySSID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(ssid);
                }
            });

            btnCopyPwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(pwd);
                }
            });


            tvSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogCallBack.confirm("",dialog);
                }
            });

            tvCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    dialogCallBack.cancel();
                }
            });

            dialog.getWindow().setContentView(view);
        }
    }
}

package org.ei.opensrp.vaksinator.imunisasiTT;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.ei.opensrp.commonregistry.AllCommonsRepository;
import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.commonregistry.CommonPersonObjectController;
import org.ei.opensrp.cursoradapter.SmartRegisterCLientsProviderForCursorAdapter;
import org.ei.opensrp.domain.Alert;
import org.ei.opensrp.repository.DetailsRepository;
import org.ei.opensrp.vaksinator.R;
import org.ei.opensrp.service.AlertService;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.dialog.FilterOption;
import org.ei.opensrp.view.dialog.ServiceModeOption;
import org.ei.opensrp.view.dialog.SortOption;
import org.ei.opensrp.view.viewHolder.OnClickFormLauncher;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Dimas Ciputra on 2/16/15.
 */
public class TTSmartClientsProvider implements SmartRegisterCLientsProviderForCursorAdapter {
    private final LayoutInflater inflater;
    private final Context context;
    private final View.OnClickListener onClickListener;
    private Drawable iconPencilDrawable;
    private final int txtColorBlack;
    private final AbsListView.LayoutParams clientViewLayoutParams;

    protected CommonPersonObjectController controller;

    AlertService alertService;
    public TTSmartClientsProvider(Context context,
                                View.OnClickListener onClickListener,
                                AlertService alertService) {
        this.onClickListener = onClickListener;
        this.context = context;
        this.alertService = alertService;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        clientViewLayoutParams = new AbsListView.LayoutParams(MATCH_PARENT,
                (int) context.getResources().getDimension(org.ei.opensrp.R.dimen.list_item_height));
        txtColorBlack = context.getResources().getColor(org.ei.opensrp.R.color.text_black);

    }

    @Override
    public void getView(SmartRegisterClient smartRegisterClient, View convertView) {

        ViewHolder viewHolder;
        if(convertView.getTag() == null || !(convertView.getTag() instanceof  ViewHolder)) {
            viewHolder = new ViewHolder();
            viewHolder.profilelayout = (LinearLayout) convertView.findViewById(R.id.profile_info_layout);

            viewHolder.wife_name = (TextView) convertView.findViewById(R.id.wife_name);
            viewHolder.husband_name = (TextView) convertView.findViewById(R.id.txt_husband_name);
            viewHolder.village_name = (TextView) convertView.findViewById(R.id.txt_village_name);
            viewHolder.wife_age = (TextView) convertView.findViewById(R.id.wife_age);
            viewHolder.no_ibu = (TextView) convertView.findViewById(R.id.no_ibu);
            viewHolder.unique_id = (TextView) convertView.findViewById(R.id.unique_id);

            viewHolder.hr_badge = (ImageView) convertView.findViewById(R.id.img_hr_badge);
            viewHolder.bpl_badge = (ImageView) convertView.findViewById(R.id.img_bpl_badge);
            viewHolder.usia_klinis = (TextView) convertView.findViewById(R.id.txt_usia_klinis);
            viewHolder.edd_due = (TextView) convertView.findViewById(R.id.lbl_htpt);
            viewHolder.htpt = (TextView) convertView.findViewById(R.id.txt_htpt);
            viewHolder.ki_lila_bb = (TextView) convertView.findViewById(R.id.txt_ki_lila_bb);
            viewHolder.beratbadan_tb = (TextView) convertView.findViewById(R.id.txt_ki_beratbadan_tb);
            viewHolder.tanggal_kunjungan_anc = (TextView) convertView.findViewById(R.id.txt_tanggal_kunjungan_anc);
            viewHolder.anc_number = (TextView) convertView.findViewById(R.id.txt_anc_number);
            viewHolder.kunjugan_ke = (TextView) convertView.findViewById(R.id.txt_kunjugan_ke);

            viewHolder.status_layout = (RelativeLayout) convertView.findViewById(R.id.anc_status_layout);
            viewHolder.status_type = (TextView) convertView.findViewById(R.id.txt_tt_status_type);
            viewHolder.status_date = (TextView) convertView.findViewById(R.id.txt_status_date_anc);
            viewHolder.alert_status = (TextView) convertView.findViewById(R.id.txt_alert_status);

            viewHolder.profilepic = (ImageView) convertView.findViewById(R.id.img_profile);
            viewHolder.follow_up = (ImageButton) convertView.findViewById(R.id.btn_edit);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.profilepic.setImageDrawable(context.getResources().getDrawable(R.mipmap.woman_placeholder));

        viewHolder.follow_up.setOnClickListener(onClickListener);
        viewHolder.follow_up.setTag(smartRegisterClient);
        viewHolder.profilelayout.setOnClickListener(onClickListener);
        viewHolder.profilelayout.setTag(smartRegisterClient);
        CommonPersonObjectClient pc = (CommonPersonObjectClient) smartRegisterClient;
        if (iconPencilDrawable == null) {
            iconPencilDrawable = context.getResources().getDrawable(R.drawable.ic_pencil);
        }
        viewHolder.follow_up.setImageDrawable(iconPencilDrawable);
        viewHolder.follow_up.setOnClickListener(onClickListener);
        //set image

        AllCommonsRepository allancRepository =  org.ei.opensrp.Context.getInstance().allCommonsRepositoryobjects("ec_ibu");
        CommonPersonObject ancobject = allancRepository.findByCaseID(pc.entityId());

        DetailsRepository detailsRepository = org.ei.opensrp.Context.getInstance().detailsRepository();
        Map<String, String> details =  detailsRepository.getAllDetailsForClient(pc.entityId());
        details.putAll(ancobject.getColumnmaps());

        if(pc.getDetails() != null) {
            pc.getDetails().putAll(details);
        }else{
            pc.setDetails(details);
        }

        final ImageView kiview = (ImageView)convertView.findViewById(R.id.img_profile);
        if (pc.getDetails().get("profilepic") != null) {
        }
        else {
            viewHolder.profilepic.setImageDrawable(context.getResources().getDrawable(R.drawable.woman_placeholder));
        }

        System.out.println("details: "+pc.getDetails().toString());

        viewHolder.wife_name.setText(pc.getColumnmaps().get("namalengkap") != null ? pc.getColumnmaps().get("namalengkap") : "");
        viewHolder.husband_name.setText(pc.getColumnmaps().get("namaSuami")!=null?pc.getColumnmaps().get("namaSuami"):"");
        viewHolder.village_name.setText(pc.getDetails().get("address1")!=null?pc.getDetails().get("address1"):"");
        viewHolder.wife_age.setText(pc.getDetails().get("umur")!=null?pc.getDetails().get("umur"):"");
        viewHolder.no_ibu.setText(pc.getDetails().get("noIbu")!=null?pc.getDetails().get("noIbu"):"");
        viewHolder.unique_id.setText(pc.getDetails().get("nik")!=null?pc.getDetails().get("nik"):"");

        viewHolder.usia_klinis.setText(pc.getDetails().get("usiaKlinis")!=null?context.getString(R.string.clinical_age)+" "+pc.getDetails().get("usiaKlinis")+" "+context.getString(R.string.week):"-");
        viewHolder.edd_due.setText(context.getString(R.string.edd_label));
        viewHolder.htpt.setText(pc.getDetails().get("htp")!=null?pc.getDetails().get("htp"):"-");

        viewHolder.ki_lila_bb.setText((pc.getDetails().get("hasilPemeriksaanLILA")!=null?pc.getDetails().get("hasilPemeriksaanLILA"):"-")+" "+context.getString(R.string.cm));
        viewHolder.beratbadan_tb.setText((pc.getDetails().get("bbKg")!=null?pc.getDetails().get("bbKg"):"-")+" "+context.getString(R.string.kg));

        String AncDate = pc.getDetails().get("ancDate")!=null?pc.getDetails().get("ancDate"):"-";
        String AncKe = pc.getDetails().get("ancKe")!=null?pc.getDetails().get("ancKe"):"-";
        String KunjunganKe = pc.getDetails().get("kunjunganKe")!=null?pc.getDetails().get("kunjunganKe"):"-";

        viewHolder.tanggal_kunjungan_anc.setText(context.getString(R.string.last_visit_date) + " " + AncDate);
        viewHolder.anc_number.setText(context.getString(R.string.anc_ke) + " " + AncKe);
        viewHolder.kunjugan_ke.setText(context.getString(R.string.visit_number) + KunjunganKe);

        viewHolder.status_type.setText("");
        viewHolder.status_date.setText("");
        viewHolder.status_layout.setBackgroundColor(context.getResources().getColor(org.ei.opensrp.R.color.status_bar_text_almost_white));
        viewHolder.alert_status.setText("");

        viewHolder.status_type.setText(pc.getDetails().get("statusImunisasitt")!= null ? ttStatus(pc.getDetails().get("statusImunisasitt")):"None");

        convertView.setLayoutParams(clientViewLayoutParams);
    }

    private String ttStatus(String status){
        if(status.contains("0"))
            return "TT 0";
        else if(status.contains("1"))
            return "TT 1";
        else if(status.contains("2"))
            return "TT 2";
        else if(status.contains("3"))
            return "TT 3";
        else if(status.contains("4"))
            return "TT 4";
        else if(status.contains("5"))
            return "TT 5";
        else
            return "None";
    }

    public SmartRegisterClients getClients() {
        return controller.getClients();
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption serviceModeOption,
                                              FilterOption searchFilter, SortOption sortOption) {
        return getClients().applyFilter(villageFilter, serviceModeOption, searchFilter, sortOption);
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
        // do nothing.
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String metaData) {
        return null;
    }

    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public View inflatelayoutForCursorAdapter() {
        View View = (ViewGroup) inflater().inflate(R.layout.smart_register_tt_client, null);
        return View;
    }

    public void risk (String risk1,String risk2,String risk3,String risk4,String risk5,String risk6,String risk7,String risk8,String risk9,String risk10,ImageView riskview){
//        if(risk1 != null && risk1.equals("yes")
//                || risk2 != null && risk2.equals("yes")
//                || risk3 != null && risk3.equals("yes")
//                || risk4 != null && risk4.equals("yes")
//                || risk5 != null && risk5.equals("yes")
//                || risk6 != null && risk6.equals("yes")
//                || risk7 != null && risk7.equals("yes")
//                || risk8 != null && risk8.equals("yes")
//                || risk9 != null && risk9.equals("yes")
//                || risk10 != null && risk10.equals("yes")){
//        }
    }
    class ViewHolder {

        TextView wife_name ;
        TextView husband_name ;
        TextView village_name;
        TextView wife_age;
        LinearLayout profilelayout;
        ImageView profilepic;
        ImageButton follow_up;
        TextView no_ibu;
        TextView unique_id;
        TextView usia_klinis;
        TextView htpt;
        TextView ki_lila_bb;
        TextView beratbadan_tb;
        TextView status_type;
        TextView status_date;
        TextView alert_status;
        RelativeLayout status_layout;
        TextView tanggal_kunjungan_anc;
        TextView anc_number;
        TextView kunjugan_ke;
        ImageView hr_badge  ;
        ImageView bpl_badge;
        TextView edd_due;
    }


}
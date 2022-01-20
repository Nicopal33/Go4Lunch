package com.example.goforlunch.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Map;
import java.util.Objects;

public class WorkmatesUiModel {

    final String uid;
    final String sentence;
    final Map<String, String> map;
    final String photo;
    final int senteceStyle;

    public WorkmatesUiModel (String id, Map<String, String> map, String sentence, String photo, int senteceStyle) {
        this.uid = id;
        this.map = map;
        this.sentence = sentence;
        this.photo = photo;
        this.senteceStyle = senteceStyle;
    }

    public String getUid() {return uid;}
    public Map<String, String> getMap() {return map;}
    public String getSentence() {return sentence;}
    public String getPhoto() {return photo;}
    public int getTxtStyle() {return senteceStyle;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkmatesUiModel workmatesUiModel = (WorkmatesUiModel) o;
        return uid.equals(workmatesUiModel.uid) &&
                sentence.equals(workmatesUiModel.sentence) &&
                photo.equals(workmatesUiModel.photo);
    }

    public static final DiffUtil.ItemCallback<WorkmatesUiModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<WorkmatesUiModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull WorkmatesUiModel oldWorkmatesUiModel,
                                               @NonNull WorkmatesUiModel newWorkmatesUiModel) {
                    return oldWorkmatesUiModel.getUid().equals(newWorkmatesUiModel.getUid());
                }

                @Override
                public boolean areContentsTheSame(@NonNull WorkmatesUiModel oldWorkmatesUiModel,
                                                  @NonNull WorkmatesUiModel newWormatesUiModel) {
                    return oldWorkmatesUiModel.equals(newWormatesUiModel);
                }
            };





}

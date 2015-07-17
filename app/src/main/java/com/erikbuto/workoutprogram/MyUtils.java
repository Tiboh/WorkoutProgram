package com.erikbuto.workoutprogram;

import com.erikbuto.workoutprogram.DB.Set;

/**
 * Created by Utilisateur on 16/07/2015.
 */
public abstract class MyUtils {

    public static String stringifySet(Set set){
        String positionStr = stringifyPositionEnglish(set.getPosition()+1);
        String rep = Integer.toString(set.getNbRep()) + " " + "REP";

        if(set.getWeight() == 0){
            return /*positionStr + " " + */rep;
        }else{
            String X = "x";
            String weight = Integer.toString(set.getWeight()) + "kg";
            return /*positionStr + " " +*/ rep + X + weight;
        }
    }

    public static String stringifyPositionEnglish(int position) {
        String positionStr;
        if(position == 11 || position == 12 || position == 13){
            positionStr = Integer.toString(position) + "TH";
        }else {
            switch (position % 10) {
                case 1:
                    positionStr = Integer.toString(position) + "ST";
                    break;
                case 2:
                    positionStr = Integer.toString(position) + "ND";
                    break;
                case 3:
                    positionStr = Integer.toString(position) + "RD";
                    break;
                default:
                    positionStr = Integer.toString(position) + "TH";
                    break;
            }
        }
        return positionStr;
    }

    public static String stringifyRestTime(int restMinute, int restSeconds){
        return restMinute + "'" + restSeconds + "''";
    }

}

package eazi.com.eazirentals.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> data1 = new ArrayList<String>();
        data1.add("You can book the bike using our booking portal.");

        List<String> data2 = new ArrayList<String>();
        data2.add("You can pick up the bike on the day of your reservation from our location. You can Check the location in contact-us page");


        List<String> data3 = new ArrayList<String>();
        data3.add("<div class=\"panel-body\">\n" +
                "The following would be the documents that need to be produced for renting the bike\n" +
                "<br><br>\n" +
                "Valid Driving License to ride a motor-cycle in India.<br>\n" +
                "Any one of the following should be submitted in original:<br><br>\n" +
                "<ul>\n" +
                "<li>Election ID Card</li>\n" +
                "<li>Passport</li>\n" +
                "<li>Pan Card</li>\n" +
                "<li>Company ID Card (Contact us to know whether your company qualifies)</li>\n" +
                "<li>Student ID of qualified colleges</li>\n" +
                "</ul>\n" +
                " <br>\n" +
                "This ID Card will be returned only at the end of rental period.\n" +
                "</div>");

        List<String> data4 = new ArrayList<String>();
        data4.add(" There will be no refund if you happen to cancel your booking. If we cancel the booking due to break down, non-availability of the bike or any other reason, we provide 100% refund of the rentals received");

        List<String> data5 = new ArrayList<String>();
        data5.add("Yes, the rider has to be at least  18 years of age with a valid driving license to ride Motorcycles.");

        List<String> data6 = new ArrayList<String>();
        data6.add("No, fuel is not included in the rental. The bike would be given with half to one liter of fuel and it can to be returned with the same amount.");

        List<String> data7 = new ArrayList<String>();
        data7.add("Yes,Though these bikes are regularly serviced and maintained they can break down. We will assist you in case there is any.");

         List<String> data8 = new ArrayList<String>();
         data8.add("No, there is no limit on kilometers ridden during the rental period.");

        List<String> data9 = new ArrayList<String>();
        data9.add("We don’t have any kind of speed limit but please follow speed limits set by the government.");

        List<String> data10 = new ArrayList<String>();
        data10.add("<div class=\"panel-body\">\n" +
                "n most of the government/private maintained state/national highways, they do not charge toll fee for motorcycles. However, if there are any exceptions to this, the rider  has to pay the toll charge involved.\n" +
                "<br><br>\n" +
                "We have the required Permit issued from the Karnataka government, permits are required to enter other States in India.\n" +
                "<br><br>\n" +
                "If any taxes are to be paid before entering a state, the rider himself has to bear the involved cost.\n" +
                "</div>");


        expandableListDetail.put("HOW DO I RESERVE A BIKE?", data1);
        expandableListDetail.put("FROM WHERE DO I PICK UP THE BIKE?", data2);
        expandableListDetail.put("WHAT ARE THE DOCUMENTS THAT I NEED TO SUBMIT TO TAKE THE BIKE?", data3);
        expandableListDetail.put("WHAT IS THE CANCELLATION POLICY?", data4);
        expandableListDetail.put("IS THERE ANY LIMIT ON THE AGE OF THE RIDER?", data5);
        expandableListDetail.put("IS FUEL INCLUDED IN THE TARIFF?", data6);
        expandableListDetail.put("CAN THESE BIKES BREAK DOWN?", data7);
        expandableListDetail.put("IS THERE ANY LIMIT ON THE NUMBER OF KILOMETERS THAT I CAN RIDE FOR A DAY?", data8);
        expandableListDetail.put("IS THERE ANY SPEED LIMIT?", data9);
        expandableListDetail.put("What is Toll/RTO taxes?", data10);
        return expandableListDetail;
    }
}

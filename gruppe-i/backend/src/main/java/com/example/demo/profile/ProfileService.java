package com.example.demo.profile;

import com.example.demo.DatasetsDB.DataSetInfoRepository;
import com.example.demo.DatasetsDB.DataSetUpDownloadService;
import com.example.demo.login.LoginService;
import com.example.demo.login.SessionRepository;
import com.example.demo.user.Privacy;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;


@Service
@Transactional
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private DataSetUpDownloadService dataSetUpDownloadService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private DataSetInfoRepository dataSetInfoRepository;

    public boolean myProfileOrPublic(Long profileID, String sessionID) {

        if (loginService.getUserBySessionID(sessionID) != null) {
            System.out.println("*****" + sessionID);

            if (!(loginService.getUserBySessionID(sessionID).getId() == profileID)) {
                if (userRepository.findById(profileID).isPresent()) {
                    if (userRepository.getReferenceById(profileID).getProfilePrivacy() == Privacy.PRIVATE) {
                        System.out.println("Profile is invisible");
                        return false;
                    }
                } else {
                    System.out.println("Profile is invisible");
                    return false;
                }

            }
        } else {
            if (userRepository.getReferenceById(profileID).getProfilePrivacy() == Privacy.PRIVATE) {
                System.out.println("Profile is invisible");
                return false;
            }
        }
        System.out.println("Profile is visible");
        return true;
    }

    public Map<String, Object> getProfilePrivacySettings(Long profileID, String sessionID){

        Map<String, Object> userPrivacySettings = new HashMap<>();

        if(userRepository.findById(profileID).isEmpty() || loginService.getUserBySessionID(sessionID) == null) return null;

        Users user = userRepository.findById(profileID).get();

        userPrivacySettings.put("isMyProfile", Objects.equals(user.getId(), loginService.getUserBySessionID(sessionID).getId()));

        if (user.getProfilePrivacy() == Privacy.PRIVATE) {
            userPrivacySettings.put("privacySetting", true);
        } else {
            userPrivacySettings.put("privacySetting", false);
        }

        if (user.getFriendListPrivacy() == Privacy.PRIVATE) {
            userPrivacySettings.put("privacySettingFriendList", true);
        } else {
            userPrivacySettings.put("privacySettingFriendList", false);
        }

        return userPrivacySettings;
    }


    @Transactional
    public void setColor(String sessionId, String color){
        Users user = this.loginService.getUserBySessionID(sessionId);

        if(user == null) return;

        user.setProfileBackgroundColor(color);

        userRepository.save(user);
        //TODO: BOOLEAN einfügen
    }

    @Transactional
    public Map<String, Object> getProfile(Long profileID, String sessionID) {

        Users user = userRepository.getReferenceById(profileID);


        Map<String, Object> userData = new HashMap<>();

        userData.put("firstname", user.getFirstname());
        userData.put("lastname", user.getLastname());
        userData.put("email", user.getEmail());
        userData.put("profilePicture", user.getProfilePicture());
        userData.put("birthday", user.getBirthday());
        userData.put("isMyProfile", Objects.equals(user.getId(), loginService.getUserBySessionID(sessionID).getId()));
        userData.put("profileBackgroundColor", user.getProfileBackgroundColor());
        userData.put("nyanCounter", user.getNyanCounter());

        if (user.getProfilePrivacy() == Privacy.PRIVATE) {
            userData.put("privacySetting", true);
        } else {
            userData.put("privacySetting", false);
        }

        if (user.getFriendListPrivacy() == Privacy.PRIVATE) {
            userData.put("friendlistPrivacySetting", true);
        } else {
            userData.put("friendlistPrivacySetting", false);
        }

        String[] favoriteDataset = new String[2];
        if (dataSetInfoRepository.getReferenceById(user.getFavouriteDatasetId()).getId() != -1) {
            favoriteDataset[0] = String.valueOf(dataSetInfoRepository.getReferenceById(user.getFavouriteDatasetId()).getId());
            favoriteDataset[1] = dataSetInfoRepository.getReferenceById(user.getFavouriteDatasetId()).getName();
            userData.put("favoriteData", favoriteDataset);
        }

        System.out.println("------ Starting to get dataSetSettings ------");
        List<DatasetSettings> firstThreeDatasetSettings = user.getProfileDatasetsSettings();
        String[][] dataSettings = new String[5][7];

        if (!firstThreeDatasetSettings.isEmpty()) {
            for (int i = 0; i < Math.min(4, firstThreeDatasetSettings.size()); i++) {
                dataSettings[i][0] = dataSetUpDownloadService.getDataset(firstThreeDatasetSettings.get(i).getDatasetId());
                dataSettings[i][1] = firstThreeDatasetSettings.get(i).getRw();
                dataSettings[i][2] = firstThreeDatasetSettings.get(i).getCol();
                dataSettings[i][3] = firstThreeDatasetSettings.get(i).getUserProfileDatasetID().toString();
                dataSettings[i][4] = firstThreeDatasetSettings.get(i).getGraphType();
                dataSettings[i][5] = firstThreeDatasetSettings.get(i).getRw_crop().toString();
                dataSettings[i][6] = dataSetInfoRepository.getReferenceById(firstThreeDatasetSettings.get(i).getDatasetId()).getName();

            }
        }


        userData.put("datasetsSettings", dataSettings);

        if (myProfile(profileID, sessionID)) {
            userData.put("myProfileBool", true);
        } else {
            userData.put("myProfileBool", false);
        }


        return userData;
    }


    public boolean giveNyan(Long id) {
        Users users = userRepository.getReferenceById(id);
        users.setNyanCounter(users.getNyanCounter()+1);

        return true;
    }

    public boolean changePrivacySetting(Long profileID, String sessionID,boolean privacySetting) {
        System.out.println("###### Privacy Setting: " + privacySetting);
        if (myProfile(profileID, sessionID)) {
            if(!privacySetting) {
                System.out.println("Changed Privacy to Open");
                userRepository.getReferenceById(profileID).setProfilePrivacy(Privacy.OPEN);
            } else {
                System.out.println("Changed Privacy to Private");
                userRepository.getReferenceById(profileID).setProfilePrivacy(Privacy.PRIVATE);
            }
            return true;
        }


        return false;
    }

    public boolean changeFriendlistPrivacySetting(Long profileID, String sessionID,boolean privacySetting) {
        System.out.println("###### Friendlist privacy Setting: " + privacySetting);
        if (myProfile(profileID, sessionID)) {
            if(!privacySetting) {
                System.out.println("Changed Privacy to Open");
                userRepository.getReferenceById(profileID).setFriendListPrivacy(Privacy.OPEN);
            } else {
                System.out.println("Changed Privacy to Private");
                userRepository.getReferenceById(profileID).setFriendListPrivacy(Privacy.PRIVATE);
            }
            return true;
        }


        return false;
    }



    public boolean myProfile(Long profileID, String sessionID) {
        if (loginService.getUserBySessionID(sessionID) != null && loginService.getUserBySessionID(sessionID).getId() == profileID) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addDTP(Long datasetID, String sessionID, Long datasetIDinProfile, String rowname, String colname,String graphType, Long rowLength) {
        Users tempUser = loginService.getUserBySessionID(sessionID);

        DatasetSettings newSet = new DatasetSettings(datasetIDinProfile, datasetID, rowname, colname, graphType, rowLength);

        List<DatasetSettings> profileDatasetsSettings = tempUser.getProfileDatasetsSettings();

        boolean foundMatch = false;
        for (int i = 0; i < profileDatasetsSettings.size(); i++) {
            DatasetSettings datasetSettings = profileDatasetsSettings.get(i);
            if (datasetSettings.getUserProfileDatasetID().equals(datasetIDinProfile)) {
                profileDatasetsSettings.set(i, newSet);
                foundMatch = true;
                break;
            }
        }

        try {
            if (foundMatch) {
                System.out.println("Das Objekt mit Dataset-ID " + datasetIDinProfile + " wurde erfolgreich ausgetauscht.");
            } else {
                profileDatasetsSettings.add(newSet);
                System.out.println("Kein Objekt mit Dataset-ID " + datasetIDinProfile + " gefunden.");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeProfileSettings(String sessionid, Long profileID, String firstname, String lastname, String email, String birthday, String newPassword, String newPasswordRepeat) {
        Users tempUser = loginService.getUserBySessionID(sessionid);

        if(tempUser.getId() != profileID) {return false;}

        tempUser.setFirstname(firstname);
        tempUser.setLastname(lastname);
        tempUser.setBirthday(birthday);
        tempUser.setEmail(email);

        if(newPassword != "DONOTSETPASSWORD" && newPasswordRepeat != "DONOTSETPASSWORD") {
            tempUser.setPassword(newPassword);
        }

        return true;
    }
}
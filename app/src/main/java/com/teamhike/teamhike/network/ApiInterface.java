package com.teamhike.teamhike.network;

import com.teamhike.teamhike.Models.Blog;
import com.teamhike.teamhike.Models.Experience;
import com.teamhike.teamhike.Models.ExperienceDetails;
import com.teamhike.teamhike.Models.FavoriteExperience;
import com.teamhike.teamhike.Models.Group;
import com.teamhike.teamhike.Models.GroupAttraction;
import com.teamhike.teamhike.Models.GroupInformation;
import com.teamhike.teamhike.Models.GroupMember;
import com.teamhike.teamhike.Models.GroupMemberTool;
import com.teamhike.teamhike.Models.GroupMessage;
import com.teamhike.teamhike.Models.GroupName;
import com.teamhike.teamhike.Models.GroupTool;
import com.teamhike.teamhike.Models.MapSearch;
import com.teamhike.teamhike.Models.Place;
import com.teamhike.teamhike.Models.Province;
import com.teamhike.teamhike.Models.Survey;
import com.teamhike.teamhike.Models.Tool;
import com.teamhike.teamhike.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("register_user.php")
    Call<User> registerUser(
            @Field("user_unique_id") String userUniqueId,
            @Field("username") String username,
            @Field("name") String name,
            @Field("email") String email,
            @Field("birthday") String birthday,
            @Field("city") String city,
            @Field("gender") String gender,
            @Field("experience") String experience,
            @Field("phone_number") String phoneNumber,
            @Field("about_me") String aboutMe,
            @Field("encoded_image") String encodedImage,
            @Field("image_path") String imagePath,
            @Field("post") String post
    );

    @FormUrlEncoded
    @POST("update_user.php")
    Call<User> updateUser(
            @Field("user_unique_id") String user_unique_id,
            @Field("username") String username,
            @Field("name") String name,
            @Field("email") String email,
            @Field("birthday") String birthday,
            @Field("city") String city,
            @Field("phone_number") String phoneNumber,
            @Field("image_path") String imagePath,
            @Field("encoded_image") String encodedImage,
            @Field("about_me") String about_me,
            @Field("logged") String logged
    );

    @FormUrlEncoded
    @POST("register_experience.php")
    Call<Experience> registerExperience(
            @Field("user_unique_id") String userUniqueId,
            @Field("experience_unique_id") String experienceUniqueId,
            @Field("location") String location,
            @Field("province") String province,
            @Field("description") String description,
            @Field("good_notes") String goodNotes,
            @Field("bad_notes") String badNotes,
            @Field("image_path") String imagePath,
            @Field("encoded_image") String encodedImage,
            @Field("views") String views,
            @Field("likes") String likes
    );

    @FormUrlEncoded
    @POST("update_experience.php")
    Call<Experience> updateExperience(
            @Field("experience_unique_id") String experienceUniqueId,
            @Field("likes") String likes
    );

    @FormUrlEncoded
    @POST("register_tool.php")
    Call<Tool> registerTool(
            @Field("user_unique_id") String user_unique_id,
            @Field("tool_name") String toolName,
            @Field("tool_number") String toolNumber
    );

    @FormUrlEncoded
    @POST("update_tool.php")
    Call<Tool> updateTool(
            @Field("user_unique_id") String user_unique_id,
            @Field("tool_name") String toolName,
            @Field("tool_number") String toolNumber
    );

    @FormUrlEncoded
    @POST("delete_tool.php")
    Call<Tool> deleteTool(
            @Field("user_unique_id") String user_unique_id,
            @Field("tool_name") String toolName
    );

    @FormUrlEncoded
    @POST("register_friend.php")
    Call<Void> addFriend(
            @Field("user_unique_id") String userUniqueId,
            @Field("friend_unique_id") String friendUniqueId
    );

    @FormUrlEncoded
    @POST("delete_friend.php")
    Call<User> deleteFriend(
            @Field("user_unique_id") String userUniqueId,
            @Field("friend_unique_id") String friendUniqueId
    );

    @FormUrlEncoded
    @POST("add_place.php")
    Call<Place> addPlace(
            @Field("user_unique_id") String userUniqueId,
            @Field("place_destination") String placeDestination
    );

    @FormUrlEncoded
    @POST("delete_place.php")
    Call<Place> deletePlace(
            @Field("user_unique_id") String phoneNumber,
            @Field("place_destination") String placeDestination
    );

    @FormUrlEncoded
    @POST("get_place_details_by_province_name.php")
    Call<List<Blog>> getPlaceDetailsByProvinceName(
            @Field("province") String province
    );

    @FormUrlEncoded
    @POST("get_favorite_places.php")
    Call<List<Place>> getFavoritePlaces(
            @Field("user_unique_id") String userUniqueId
    );

    @FormUrlEncoded
    @POST("register_survey_catchup.php")
    Call<Void> registerSurveyCatchup(
            @Field("user_unique_id") String userUniqueId,
            @Field("favorite_attraction") String favAttraction,
            @Field("favorite_natural_attraction") String favNaturalAttraction,
            @Field("travel_with") String travelWith,
            @Field("travel_type") String travelType
    );

    @FormUrlEncoded
    @POST("get_user_from_phone_number.php")
    Call<User> getUserFromPhoneNumber(
            @Field("phone_number") String userPhoneNumber
    );

    @FormUrlEncoded
    @POST("get_user_from_unique_id.php")
    Call<User> getUserFromUniqueId(
            @Field("user_unique_id") String userUniqueId
    );

    @FormUrlEncoded
    @POST("get_user_experiences_by_unique_id.php")
    Call<List<Experience>> getUserExperiencesByUniqueId(
            @Field("user_unique_id") String userUniqueId
    );

    @FormUrlEncoded
    @POST("get_tools.php")
    Call<List<Tool>> getTools(
            @Field("user_unique_id") String user_unique_id
    );

    @FormUrlEncoded
    @POST("get_friends.php")
    Call<List<User>> getFriends(
            @Field("user_unique_id") String userUniqueId
    );

    @FormUrlEncoded
    @POST("get_survey.php")
    Call<Survey> getSurvey(
            @Field("owner") String phoneNumber
    );

    @FormUrlEncoded
    @POST("register_group.php")
    Call<Group> registerGroup(
            @Field("group_unique_id") String groupUniqueId,
            @Field("creator_unique_id") String creatorUniqueId,
            @Field("leader_unique_id") String leaderUniqueId,
            @Field("destination_province") String destinationProvince,
            @Field("map_longitude") String mapLongitude,
            @Field("map_latitude") String mapLatitude,
            @Field("travel_hardness_level") String travelHardnessLevel
    );

    @FormUrlEncoded
    @POST("add_group_member.php")
    Call<GroupMember> addGroupMember(
            @Field("group_unique_id") String groupUniqueId,
            @Field("member_unique_id") String memberUniqueId
    );

    @FormUrlEncoded
    @POST("register_group_attractions.php")
    Call<GroupAttraction> registerGroupAttractions(
            @Field("group_unique_id") String group_unique_id,
            @Field("attraction_1") String attraction_1,
            @Field("attraction_2") String attraction_2,
            @Field("attraction_3") String attraction_3,
            @Field("attraction_4") String attraction_4,
            @Field("attraction_5") String attraction_5,
            @Field("attraction_6") String attraction_6,
            @Field("attraction_7") String attraction_7,
            @Field("attraction_8") String attraction_8,
            @Field("attraction_9") String attraction_9
    );

    @FormUrlEncoded
    @POST("register_group_information.php")
    Call<GroupInformation> registerGroupInformation(
            @Field("group_unique_id") String groupUniqueId,
            @Field("minimum_member") String minimumMember,
            @Field("maximum_member") String maximumMember,
            @Field("start_date") String startDate,
            @Field("finish_date") String finishDate,
            @Field("start_place") String startPlace,
            @Field("finish_place") String finishPlace,
            @Field("start_time") String startTime,
            @Field("finish_time") String finishTime,
            @Field("target_place_1") String targetPlace_1,
            @Field("target_place_2") String targetPlace_2,
            @Field("target_place_3") String targetPlace_3,
            @Field("target_place_4") String targetPlace_4,
            @Field("target_place_5") String targetPlace_5,
            @Field("needed_on_way") String neededOnWay,
            @Field("meals") String meals,
            @Field("more_notes") String moreNotes
    );

    @FormUrlEncoded
    @POST("register_group_member_tool.php")
    Call<GroupMemberTool> registerGroupMemberTool(
            @Field("group_unique_id") String groupUniqueId,
            @Field("tool_name") String toolName,
            @Field("tool_number") String tool_number,
            @Field("tool_type") String tool_type
    );

    @FormUrlEncoded
    @POST("register_group_tool.php")
    Call<GroupTool> registerGroupTool(
            @Field("group_unique_id") String groupUniqueId,
            @Field("tool_name") String toolName,
            @Field("tool_number") String toolNumber,
            @Field("tool_type") String tooltype
    );

    @FormUrlEncoded
    @POST("register_group_name.php")
    Call<GroupName> registerGroupName(
            @Field("group_unique_id") String groupUniqueId,
            @Field("group_name") String groupName,
            @Field("group_image") String groupImage,
            @Field("encoded_image") String encodedImage
    );

    @FormUrlEncoded
    @POST("get_group.php")
    Call<Group> getGroup(
            @Field("group_unique_id") String groupUniqueId
    );

    @FormUrlEncoded
    @POST("get_group_information.php")
    Call<GroupInformation> getGroupInformation(
            @Field("group_unique_id") String groupUniqueId
    );

    @FormUrlEncoded
    @POST("get_group_name.php")
    Call<GroupName> getGroupName(
            @Field("group_unique_id") String groupUniqueId
    );

    @FormUrlEncoded
    @POST("set_group_situation.php")
    Call<Void> setGroupSituation(
            @Field("group_unique_id") String groupUniqueId,
            @Field("situation") Integer situation
    );

    @FormUrlEncoded
    @POST("get_all_groups_by_creator_unique_id.php")
    Call<List<Group>> getAllGroupsByCreatorUniqueId(
            @Field("creator_unique_id") String creatorUniqueId
    );

    @FormUrlEncoded
    @POST("send_experience_comment.php")
    Call<ExperienceDetails> sendExperienceComment(
            @Field("user_unique_id") String userUniqueId,
            @Field("experience_unique_id") String experienceUniqueId,
            @Field("sender_unique_id") String senderUniqueId,
            @Field("comment") String comment
    );

    @FormUrlEncoded
    @POST("get_all_experiences_details_by_unique_id.php")
    Call<List<ExperienceDetails>> getExperienceDetailsByUniqueId(
            @Field("experience_unique_id") String experienceUniqueId
    );

    @FormUrlEncoded
    @POST("save_favorite_experience.php")
    Call<FavoriteExperience> saveFavoriteExperience(
            @Field("experience_unique_id") String experienceUniqueId,
            @Field("user_unique_id") String userUniqueId
    );

    @FormUrlEncoded
    @POST("delete_favorite_experience.php")
    Call<FavoriteExperience> deleteFavoriteExperience(
            @Field("experience_unique_id") String experienceUniqueId,
            @Field("user_unique_id") String userUniqueId
    );

    @FormUrlEncoded
    @POST("get_user_favorite_experiences.php")
    Call<List<FavoriteExperience>> getUserFavoriteExperiences(
            @Field("user_unique_id") String userUniqueId
    );

    @FormUrlEncoded
    @POST("register_blog.php")
    Call<Blog> registerBlog(
            @Field("blog_unique_id") String blogUniqueId,
            @Field("province") String province,
            @Field("destination") String destination,
            @Field("image") String image,
            @Field("encoded_image") String encodedImage,
            @Field("description") String description,
            @Field("static_location_image") String static_location_image,
            @Field("encoded_static_location_image") String encoded_static_location_image
    );

    @FormUrlEncoded
    @POST("get_experiences_by_province_name.php")
    Call<List<Experience>> getExperiencesByProvinceName(
            @Field("province") String province
    );

    @FormUrlEncoded
    @POST("update_group_leader.php")
    Call<Void> updateGroupLeader(
            @Field("leader_unique_id") String leaderUniqueId,
            @Field("group_unique_id") String groupUniqueId
    );

    @FormUrlEncoded
    @POST("send_group_message.php")
    Call<GroupMessage> sendGroupMessage(
            @Field("group_unique_id") String groupUniqueId,
            @Field("sender_unique_id") String senderUniqueId,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST("get_group_chats_by_group_unique_id.php")
    Call<List<GroupMessage>> getGroupChats(
            @Field("group_unique_id") String groupUniqueId
    );

    @GET("get_all_users.php")
    Call<List<User>> getAllUsers();

    @GET("get_all_places.php")
    Call<List<Place>> getAllPlaces();

    @Headers("Api-key: service.l3ySjWwovfZh8ozeezTaMo9tESmmE4nmpQy6QQPc")
    @GET
    Call<MapSearch> getSearch(@Url String url);

    @GET("get_provinces_name.php")
    Call<List<Province>> getProvinceName();

    @GET("get_all_leaders.php")
    Call<List<User>> getAllLeaders();

    @GET("get_all_group_members.php")
    Call<List<User>> getAllGroupMembers();

    @GET("get_all_experiences.php")
    Call<List<Experience>> getAllExperience();

    @GET("get_experiences_details.php")
    Call<List<ExperienceDetails>> getExperienceDetails();

    @GET("get_all_favorite_experiences.php")
    Call<List<FavoriteExperience>> getAllFavoriteExperiences();

    @GET("get_all_blog.php")
    Call<List<Blog>> getAllBlog();

    @GET("get_all_pending_groups.php")
    Call<List<Group>> getAllPendingGroups();

    @GET("get_all_accepted_groups.php")
    Call<List<Group>> getAllAcceptedGroups();
}

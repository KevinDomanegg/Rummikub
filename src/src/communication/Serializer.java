package communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import communication.gameinfo.GameInfo;
import communication.gameinfo.GameUsernames;
import communication.gameinfo.GridInfo;
import communication.gameinfo.StoneInfo;
import communication.request.ConcreteMove;
import communication.request.Request;
import communication.request.RequestID;

/**
 * Class serializing Request- and GameInfo-Objects to Json's.
 */
public class Serializer {

  public static void main(String[] args) {
    Serializer ser = new Serializer();
    String requestJson = (ser.serialize(new ConcreteMove(RequestID.HAND_MOVE, 0, 0, 10 ,10)));
    String infoJson = (ser.serialize(new GameUsernames("Johannes", 0)));

    System.out.println(requestJson);
    System.out.println(infoJson);

    Deserializer deser = new Deserializer();
    GameInfo info = deser.deserializeInfo(infoJson);
    Request request = deser.deserializeRequest(requestJson);

    System.out.println(info);
    System.out.println(request);
  }

  /**
   * Serializes a Request.
   *
   * @param request to be serialized
   * @return serialized representation of the Request
   */
  public String serialize(Request request) {

    Gson gson;
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Request.class, new InterfaceAdapter<Request>());
    gson = builder.create();

    return gson.toJson(request, Request.class);
  }

  /**
   * Serializes a GameInfo.
   *
   * @param gameInfo to be serialized
   * @return serialized representation of the GameInfo
   */
  public String serialize(GameInfo gameInfo) {
    Gson gson;
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(GameInfo.class, new InterfaceAdapter<GameInfo>());
    gson = builder.create();
    return gson.toJson(gameInfo, GameInfo.class);
  }
}

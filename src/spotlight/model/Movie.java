package spotlight.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import spotlight.exceptions.ExceptionDialog;
import spotlight.ffmpeg.FFmpeg;
import spotlight.util.BuildLink;
import spotlight.util.Setting;
import spotlight.util.ToClient;

public class Movie implements Serializable {
	private static final long serialVersionUID = 1L;
	Boolean adult;
	String backdrop_path;
	Object belongs_to_collection;
	String genres;
	Integer budget;
	String homepage;
	Integer id;
	String imdb_id;
	String original_language;
	String original_title;
	String overview;
	String movie_path;
	Double popularity;
	String poster_path;
	String release_date;
	Long revenue;
	Integer runtime;
	String status;
	String tagline;
	String title;
	Boolean video;
	Number vote_average;
	Integer vote_count;

	// Dati del video presi da ffmpeg
	String codec_name;
	String codec_long_name;
	Long width;
	Long height;
	String display_aspect_ratio;
	String avg_frame_rate;
	Integer duration;
	Long size;
	String format_name;
	String format_long_name;
	Integer bit_rate;
	String encoder;

	/*
	 * @FXML private Button removeButton;
	 * 
	 * @FXML private Button deleteButton;
	 * 
	 * @FXML private Text filename;
	 * 
	 * @FXML private Text fileSize;
	 * 
	 * @FXML private Text title;
	 * 
	 * @FXML private Text nominal_runtime;
	 * 
	 * @FXML private Text effective_runtime;
	 * 
	 * @FXML private Text codec;
	 * 
	 * @FXML private Text resolution;
	 * 
	 * @FXML private Text proportion;
	 * 
	 * @FXML private Text format;
	 * 
	 * @FXML private Text fps;
	 * 
	 * @FXML private Text bps;
	 * 
	 * @FXML private Text encoder;
	 */

	public Movie(JSONObject jobj, String movie_path) {
		super();
		this.movie_path = movie_path;
		TakeFilmInfo(jobj);
		TakeFFmpegInfo(FFmpeg.getData(movie_path));
	}

	public Movie(Integer id, String movie_path) {
		super();
		try {
			this.movie_path = movie_path;
			if (id != null) {
				// System.out.println("ID film: " + id);
				String link = BuildLink.GetDetails(id);
				// System.out.println("Secondo link: " + link);
				String response = ToClient.Response(link);
				if (response != null) {
					JSONParser parser = new JSONParser();
					JSONObject jobj = (JSONObject) parser.parse(response);
					TakeFilmInfo(jobj);
				} else {
					TakeFilmInfo(null);
				}
			}
			if (movie_path != null) {
				TakeFFmpegInfo(FFmpeg.getData(movie_path));
			}
		} catch (ResourceException | ParseException e) {
			ExceptionDialog.show(e);
			e.printStackTrace();
		}
	}

	public void TakeFilmInfo(JSONObject jobj) {
		if (jobj != null) {
			JSONArray jArray = new JSONArray();
			JSONObject j_temp = null;

			adult = (Boolean) jobj.get("adult");
			backdrop_path = (String) jobj.get("backdrop_path");
			belongs_to_collection = (Object) jobj.get("belongs_to_collection");

			jArray.addAll((Collection) jobj.get("genres"));
			genres = "";
			for (int i = 0; i < jArray.size(); i++) {
				if (jArray.get(i) != null) {
					j_temp = (JSONObject) jArray.get(i);

					if (i + 1 == jArray.size())
						genres = genres.concat(j_temp.get("name").toString());
					else
						genres = genres.concat(j_temp.get("name").toString() + ", ");
				}
			}

			if (jobj.get("budget") != null)
				budget = Integer.parseInt(jobj.get("budget").toString());

			homepage = (String) jobj.get("homepage");

			if (jobj.get("id") != null)
				id = Integer.parseInt(jobj.get("id").toString());

			imdb_id = (String) jobj.get("imdb_id");
			original_language = (String) jobj.get("original_language");
			original_title = (String) jobj.get("original_title");
			overview = (String) jobj.get("overview");
			popularity = (Double) jobj.get("popularity");
			poster_path = (String) jobj.get("poster_path");

			status = (String) jobj.get("status");

			if (jobj.get("revenue") != null)
				revenue = Long.parseLong(jobj.get("revenue").toString());

			if (jobj.get("runtime") != null)
				runtime = Integer.parseInt(jobj.get("runtime").toString());

			release_date = (String) jobj.get("release_date");
			tagline = (String) jobj.get("tagline");
			title = (String) jobj.get("title");
			video = (Boolean) jobj.get("video");
			vote_average = (Number) jobj.get("vote_average");

			if (jobj.get("vote_count") != null)
				vote_count = Integer.parseInt(jobj.get("vote_count").toString());
		}
	}

	public void TakeFFmpegInfo(JSONObject jobj) {
		codec_name = null;
		codec_long_name = null;
		width = null;
		height = null;
		display_aspect_ratio = null;
		avg_frame_rate = null;
		duration = null;
		size = null;
		format_name = null;
		format_long_name = null;
		bit_rate = null;
		encoder = null;
		if (jobj != null && jobj.get("streams") != null) {
			JSONArray jArray = new JSONArray();
			JSONObject tobj = null;

			jArray.addAll((Collection) jobj.get("streams"));
			for (int i = 0; i < jArray.size(); i++) {
				tobj = (JSONObject) jArray.get(i);
				if (tobj.get("codec_type").equals("video")) {
					break;
				}
			}
			if (tobj.get("codec_name") != null)
				codec_name = (String) tobj.get("codec_name");

			if (tobj.get("codec_long_name") != null)
				codec_long_name = (String) tobj.get("codec_long_name");

			if (tobj.get("width") != null)
				width = (Long) tobj.get("width");

			if (tobj.get("height") != null)
				height = (Long) tobj.get("height");

			if (tobj.get("display_aspect_ratio") != null)
				display_aspect_ratio = (String) tobj.get("display_aspect_ratio");

			if (tobj.get("avg_frame_rate") != null) {
				String temp = (String) tobj.get("avg_frame_rate");

				Double num = Double.parseDouble(temp.split("/")[0]);
				Double den = Double.parseDouble(temp.split("/")[1]);

				avg_frame_rate = Setting.df.format(num / den); // questo mostro serve per avere solo 2 cifre
																// significative
			}

			if (jobj.get("format") != null) {
				tobj = (JSONObject) jobj.get("format");

				if (tobj.get("duration") != null)
					duration = new Integer((int) Double.parseDouble(tobj.get("duration").toString()));

				if (tobj.get("size") != null)
					size = Long.parseLong(tobj.get("size").toString());

				if (tobj.get("format_name") != null)
					format_name = (String) tobj.get("format_name");

				if (tobj.get("format_name") != null)
					format_long_name = (String) tobj.get("format_long_name");

				if (tobj.get("bit_rate") != null)
					bit_rate = Integer.parseInt(tobj.get("bit_rate").toString());
			}
			if (tobj.get("tags") != null) {
				tobj = (JSONObject) tobj.get("tags");
				if (tobj.get("encoder") != null)
					encoder = (String) tobj.get("encoder");
			}
		}
	}

	public void addInfo(Movie tmp) {
		if (codec_name == null)
			codec_name = tmp.getCodec_name();

		if (codec_long_name == null)
			codec_long_name = tmp.getCodec_long_name();

		if (width == null)
			width = tmp.getWidth();

		if (height == null)
			height = tmp.getHeight();

		if (display_aspect_ratio == null)
			display_aspect_ratio = tmp.getDisplay_aspect_ratio();

		if (avg_frame_rate == null)
			avg_frame_rate = tmp.getAvg_frame_rate();

		if (duration == null)
			duration = tmp.getDuration();

		if (size == null)
			size = tmp.getSize();

		if (format_name == null)
			format_name = tmp.getFormat_name();

		if (format_long_name == null)
			format_long_name = tmp.getFormat_long_name();

		if (bit_rate == null)
			bit_rate = tmp.getBit_rate();

		if (encoder == null)
			encoder = tmp.getEncoder();
	}

	@Override
	public String toString() {
		return "Movie\nadult=" + adult + "\nbackdrop_path=" + backdrop_path + "\nbelongs_to_collection="
				+ belongs_to_collection + "\ngenres=" + genres + "\nbudget=" + budget + "\nhomepage=" + homepage
				+ "\nid=" + id + "\nimdb_id=" + imdb_id + "\noriginal_language=" + original_language
				+ "\noriginal_title=" + original_title + "\noverview=" + overview + "\nmovie_path=" + movie_path
				+ "\npopularity=" + popularity + "\nposter_path=" + poster_path + "\nrelease_date=" + release_date
				+ "\nrevenue=" + revenue + "\nruntime=" + runtime + "\nstatus=" + status + "\ntagline=" + tagline
				+ "\ntitle=" + title + "\nvideo=" + video + "\nvote_average=" + vote_average + "\nvote_count="
				+ vote_count + "\ncodec_name=" + codec_name + "\ncodec_long_name=" + codec_long_name + "\nwidth="
				+ width + "\nheight=" + height + "\ndisplay_aspect_ratio=" + display_aspect_ratio + "\navg_frame_rate="
				+ avg_frame_rate + "\nduration=" + duration + "\nsize=" + size + "\nformat_name=" + format_name
				+ "\nformat_long_name=" + format_long_name + "\nbit_rate=" + bit_rate + "\nencoder=" + encoder;
	}

	public String poster_path() {
		return poster_path;
	}

	public String getOriginal_language() {
		return original_language;
	}

	public String getOriginal_title() {
		return original_title;
	}

	public String getOverview() {
		return overview;
	}

	public Integer getRuntime() {
		return runtime;
	}

	public Number getVote_average() {
		return vote_average;
	}

	public Integer getVote_count() {
		return vote_count;
	}

	public String getMovie_path() {
		return movie_path;
	}

	public void setMovie_path(String movie_path) {
		this.movie_path = movie_path;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public String getTitle() {
		return title;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getAdult() {
		return adult;
	}

	public String getBackdrop_path() {
		return backdrop_path;
	}

	public Object getBelongs_to_collection() {
		return belongs_to_collection;
	}

	public String getGenres() {
		return genres;
	}

	public Integer getBudget() {
		return budget;
	}

	public String getHomepage() {
		return homepage;
	}

	public Integer getId() {
		return id;
	}

	public String getImdb_id() {
		return imdb_id;
	}

	public Double getPopularity() {
		return popularity;
	}

	public String getRelease_date() {
		return release_date;
	}

	public Long getRevenue() {
		return revenue;
	}

	public String getStatus() {
		return status;
	}

	public String getTagline() {
		return tagline;
	}

	public Boolean getVideo() {
		return video;
	}

	public String getCodec_name() {
		return codec_name;
	}

	public String getCodec_long_name() {
		return codec_long_name;
	}

	public Long getWidth() {
		return width;
	}

	public Long getHeight() {
		return height;
	}

	public String getDisplay_aspect_ratio() {
		return display_aspect_ratio;
	}

	public String getAvg_frame_rate() {
		return avg_frame_rate;
	}

	public Integer getDuration() {
		return duration;
	}

	public Long getSize() {
		return size;
	}

	public String getFormat_name() {
		return format_name;
	}

	public String getFormat_long_name() {
		return format_long_name;
	}

	public Integer getBit_rate() {
		return bit_rate;
	}

	public String getEncoder() {
		return encoder;
	}

}

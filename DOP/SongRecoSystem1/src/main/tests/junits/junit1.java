package junits;

import static org.junit.Assert.*;

import java.util.List;

import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import MongoEntities.AlbumBean;
import MongoEntities.ArtistDataBean;
import MongoEntities.SongBean;

import com.sample.Connectivity;
import com.sample.InvokeRules;

public class junit1 {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("restriction")
	@Test
	public void testFetchSongDataForSong() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<SongBean> l2=ruleInvoker.fetchSongDataForSong(ksession, "song_417501", "SongData");
		assertEquals(l2.get(0).getSongID(), "song_417501");
		//fail("Not yet implemented");
	}

	@SuppressWarnings("restriction")
	@Test
	public void testFetchSongDataForArtist() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<SongBean> l2=ruleInvoker.fetchSongDataForArtist(ksession, connect.getPreferredArtist("user_99999"), "SongFromArtist");
		assertEquals(l2.get(0).getSongID(), "song_234506");
		//fail("Not yet implemented");
	}

	@Test
	public void testFetchSongDataForAlbum() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<SongBean> l2=ruleInvoker.fetchSongDataForAlbum(ksession, "album_1", "SongFromAlbum");
		assertEquals(l2.get(0).getSongID(), "song_1");
		//fail("Not yet implemented");
	}

	@Test
	public void testFetchArtistData() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<ArtistDataBean> l=ruleInvoker.fetchArtistData(ksession, "artist_417501", "ArtistDetails");
		assertEquals(l.get(0).getArtistId(), "artist_417501");
	}

	@Test
	public void testfetchArtistDataforSong() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<ArtistDataBean> l=ruleInvoker.fetchArtistDataforSong(ksession, "song_417501", "ArtistDetails");
		assertEquals(l.get(0).getArtistId(), "artist_417501");
	}

	@Test
	public void testfetchArtistDataForGenre() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<ArtistDataBean> l=ruleInvoker.fetchArtistDataForGenre(ksession, "genre_8", "ArtistFromGenre");
		assertEquals(l.size(), 20144);
	}

	@Test
	public void testfetchAlbumDataForArtist() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<AlbumBean> l=ruleInvoker.fetchAlbumDataForArtist(ksession, "artist_417501", "AlbumFromArtist");
		assertEquals(l.get(0).getAlbumId(), "album_417501");
	}

	@Test
	public void testfetchAlbumDataForAlbum() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<AlbumBean> l=ruleInvoker.fetchAlbumDataForAlbum(ksession, "album_417501", "AlbumDetails");
		assertEquals(l.get(0).getAlbumId(), "album_417501");
	}

	@Test
	public void testfetchAlbumDataforSong() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<AlbumBean> l=ruleInvoker.fetchAlbumDataforSong(ksession, "song_417501", "AlbumDetails");
		assertEquals(l.get(0).getAlbumId(), "album_417501");
	}

	@Test
	public void testfetchAlbumDataForGenre() throws Exception {
		Connectivity connect = new Connectivity();
		connect.initializeParams();
		StatefulKnowledgeSession ksession = connect.getKnowledgeSession();
		InvokeRules ruleInvoker = new InvokeRules();
		List<AlbumBean> l=ruleInvoker.fetchAlbumDataForGenre(ksession, "genre_8", "AlbumFromGenre");
		assertEquals(l.size(), 20144);
	}
}

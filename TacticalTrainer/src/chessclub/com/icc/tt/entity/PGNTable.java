package chessclub.com.icc.tt.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="pgntable")
public class PGNTable {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pgntable_seq")
    @SequenceGenerator(name = "pgntable_seq", sequenceName = "pgntable_seq")
	private int id;
	@Lob
	private String pgndata;
	private String white;
	private String black;
    private Date played;
    @OneToMany(mappedBy="pgn")
    private Set<TacticsTable> tactics;
}

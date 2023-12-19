package Management.TenantAdmin.Service;
import java.util.Objects;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import Management.TenantAdmin.Entity.DbSequence;

@Service
public class SequenceGeneratorService {

	private static MongoOperations mongoOperations;

	
	public SequenceGeneratorService(MongoOperations mongoOperations) {
		SequenceGeneratorService.mongoOperations = mongoOperations;
	}

	public static int getSeqNumber(String sequenceName) {

		Query query = new Query(Criteria.where("id").is(sequenceName));
		Update update = new Update().inc("seqNo", 1);

		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		options.upsert(true);

		DbSequence counter = mongoOperations.findAndModify(query, update, options, DbSequence.class);
		return !Objects.isNull(counter) ? counter.getSeqNo() : 1;

	}

}

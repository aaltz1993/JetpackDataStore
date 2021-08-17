package a.alt.z.datastore

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserSerializer: Serializer<User> {

    override fun readFrom(input: InputStream): User {
        return try { User.parseFrom(input) }
        catch (exception: InvalidProtocolBufferException) { throw CorruptionException("failed deserializing protocol", exception) }
    }

    override fun writeTo(t: User, output: OutputStream) = t.writeTo(output)
}
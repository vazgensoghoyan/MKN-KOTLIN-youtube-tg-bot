package bot

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class UserCommandManager {
    private val userJobs = ConcurrentHashMap<Long, Job>()

    suspend fun execute(
        userId: Long,
        block: suspend () -> Unit,
    ) {
        userJobs[userId]?.cancel() // Отменяем предыдущую команду
        userJobs[userId] =
            coroutineScope {
                launch {
                    try {
                        block()
                    } finally {
                        userJobs.remove(userId)
                    }
                }
            }
    }
}

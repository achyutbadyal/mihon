package kikyo.domain.extensionrepo.interactor

import kikyo.domain.extensionrepo.model.ExtensionRepo
import kikyo.domain.extensionrepo.repository.ExtensionRepoRepository

class ReplaceExtensionRepo(
    private val repository: ExtensionRepoRepository,
) {
    suspend fun await(repo: ExtensionRepo) {
        repository.replaceRepo(repo)
    }
}

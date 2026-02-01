package kikyo.domain.extensionrepo.interactor

import kikyo.domain.extensionrepo.repository.ExtensionRepoRepository

class DeleteExtensionRepo(
    private val repository: ExtensionRepoRepository,
) {
    suspend fun await(baseUrl: String) {
        repository.deleteRepo(baseUrl)
    }
}

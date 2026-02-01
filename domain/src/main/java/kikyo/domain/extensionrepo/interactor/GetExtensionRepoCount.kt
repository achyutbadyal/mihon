package kikyo.domain.extensionrepo.interactor

import kikyo.domain.extensionrepo.repository.ExtensionRepoRepository

class GetExtensionRepoCount(
    private val repository: ExtensionRepoRepository,
) {
    fun subscribe() = repository.getCount()
}

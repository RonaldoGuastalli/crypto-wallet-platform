package ch.mycrypto.cryptowalletapi.infrastructure.controller;

import ch.mycrypto.cryptowalletapi.application.wallet.addAsset.AddAssetToWalletUseCase;
import ch.mycrypto.cryptowalletapi.application.wallet.create.WalletCreatorUseCase;
import ch.mycrypto.cryptowalletapi.application.wallet.evaluate.EvaluateWalletUseCase;
import ch.mycrypto.cryptowalletapi.application.wallet.get.GetWalletUseCase;
import ch.mycrypto.cryptowalletapi.infrastructure.mapper.ControllerMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AddAssetHttpRequest;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.asset.model.AssetHttpResponse;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.mapper.WalletEvaluationMapper;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.CreateWalletHttpRequest;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletEvaluationHttpRequest;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletEvaluationHttpResponse;
import ch.mycrypto.cryptowalletapi.infrastructure.wallet.model.WalletHttpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static ch.mycrypto.cryptowalletapi.infrastructure.mapper.ControllerMapper.toResponse;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class WalletController {

    private final WalletCreatorUseCase walletCreatorUseCase;
    private final AddAssetToWalletUseCase addAssetToWalletUseCase;
    private final GetWalletUseCase getWalletUseCase;
    private final EvaluateWalletUseCase evaluateWalletUseCase;
    private final WalletEvaluationMapper walletEvaluationMapper;

    @PostMapping("/wallets")
    public ResponseEntity<WalletHttpResponse> createWallet(
            @Valid @RequestBody CreateWalletHttpRequest request
    ) {
        final var input = new WalletCreatorUseCase.Input(null, request.email());
        WalletCreatorUseCase.Output output = walletCreatorUseCase.execute(input);
        WalletHttpResponse response = toResponse(output);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/wallets/{walletId}/assets")
    public ResponseEntity<WalletHttpResponse> addAsset(
            @PathVariable String walletId,
            @Valid @RequestBody AddAssetHttpRequest request) {

        AddAssetToWalletUseCase.Input input = new AddAssetToWalletUseCase.Input(
                walletId,
                request.symbol(),
                request.price(),
                request.quantity()
        );
        AddAssetToWalletUseCase.Output output = addAssetToWalletUseCase.execute(input);
        WalletHttpResponse response = toResponse(output);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/wallets/{id}")
    public ResponseEntity<WalletHttpResponse> getWallet(@PathVariable String id) {
        var output = getWalletUseCase.execute(new GetWalletUseCase.Input(id));
        var wallet = output.wallet();

        Set<AssetHttpResponse> assets = wallet.assets().stream()
                .map(ControllerMapper::toResponse)
                .collect(Collectors.toSet());

        WalletHttpResponse response = new WalletHttpResponse(
                wallet.id(),
                wallet.getTotalValue(),
                assets
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/wallets/evaluations")
    public ResponseEntity<WalletEvaluationHttpResponse> evaluate(@RequestBody @Valid WalletEvaluationHttpRequest request) {
        var input = walletEvaluationMapper.toInput(request);
        var output = evaluateWalletUseCase.execute(input);
        return ResponseEntity.ok(walletEvaluationMapper.toResponse(output));
    }

}

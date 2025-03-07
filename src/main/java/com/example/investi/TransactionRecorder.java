package com.example.investi;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.9.4.
 */
@SuppressWarnings("rawtypes")
public class TransactionRecorder extends Contract {
    public static final String BINARY = "6080604052348015600e575f80fd5b506102268061001c5f395ff3fe608060405234801561000f575f80fd5b5060043610610029575f3560e01c8063a8a476921461002d575b5f80fd5b61004061003b3660046100c7565b610042565b005b836001600160a01b0316856001600160a01b03167fed6cd76346886f3175bab2cc3288b08d501002fc079d462083a969a6b2d18966858585604051610089939291906101ad565b60405180910390a35050505050565b80356001600160a01b03811681146100ae575f80fd5b919050565b634e487b7160e01b5f52604160045260245ffd5b5f805f805f60a086880312156100db575f80fd5b6100e486610098565b94506100f260208701610098565b93506040860135925060608601359150608086013567ffffffffffffffff81111561011b575f80fd5b8601601f8101881361012b575f80fd5b803567ffffffffffffffff811115610145576101456100b3565b604051601f8201601f19908116603f0116810167ffffffffffffffff81118282101715610174576101746100b3565b6040528181528282016020018a101561018b575f80fd5b816020840160208301375f602083830101528093505050509295509295909350565b838152826020820152606060408201525f82518060608401528060208501608085015e5f608082850101526080601f19601f83011684010191505094935050505056fea2646970667358221220350ffcb41a43c58aa223332542329f2ab497dcff4478ae007e1073600215153264736f6c634300081a0033";

    public static final String FUNC_RECORDTRANSACTION = "recordTransaction";

    public static final Event TRANSACTIONRECORDED_EVENT = new Event("TransactionRecorded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected TransactionRecorder(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TransactionRecorder(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TransactionRecorder(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TransactionRecorder(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<TransactionRecordedEventResponse> getTransactionRecordedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSACTIONRECORDED_EVENT, transactionReceipt);
        ArrayList<TransactionRecordedEventResponse> responses = new ArrayList<TransactionRecordedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransactionRecordedEventResponse typedResponse = new TransactionRecordedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.receiver = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransactionRecordedEventResponse> transactionRecordedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransactionRecordedEventResponse>() {
            @Override
            public TransactionRecordedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSACTIONRECORDED_EVENT, log);
                TransactionRecordedEventResponse typedResponse = new TransactionRecordedEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.receiver = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.fee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.description = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransactionRecordedEventResponse> transactionRecordedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSACTIONRECORDED_EVENT));
        return transactionRecordedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> recordTransaction(String _sender, String _receiver, BigInteger _amount, BigInteger _fee, String _description) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RECORDTRANSACTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _sender), 
                new org.web3j.abi.datatypes.Address(160, _receiver), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount), 
                new org.web3j.abi.datatypes.generated.Uint256(_fee), 
                new org.web3j.abi.datatypes.Utf8String(_description)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static TransactionRecorder load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransactionRecorder(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TransactionRecorder load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TransactionRecorder(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TransactionRecorder load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TransactionRecorder(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TransactionRecorder load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TransactionRecorder(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TransactionRecorder> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactionRecorder.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TransactionRecorder> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TransactionRecorder.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<TransactionRecorder> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TransactionRecorder.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<TransactionRecorder> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TransactionRecorder.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class TransactionRecordedEventResponse extends BaseEventResponse {
        public String sender;

        public String receiver;

        public BigInteger amount;

        public BigInteger fee;

        public String description;
    }
}

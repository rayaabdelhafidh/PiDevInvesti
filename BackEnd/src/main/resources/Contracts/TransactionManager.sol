pragma solidity ^0.8.0;

contract TransactionRecorder {
    event TransactionRecorded(
        address indexed sender,
        address indexed receiver,
        uint256 amount,
        uint256 fee,
        string description
    );

    function recordTransaction(
        address _sender,
        address _receiver,
        uint256 _amount,
        uint256 _fee,
        string memory _description
    ) public {
        emit TransactionRecorded(_sender, _receiver, _amount, _fee, _description);
    }
}
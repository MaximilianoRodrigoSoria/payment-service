package com.ar.laboratory.paymentservice.example.application.inbound.command;

import com.ar.laboratory.paymentservice.example.domain.model.Example;

/** Puerto de entrada para crear un Example */
public interface CreateExampleCommand {

    Example execute(Example example);
}
